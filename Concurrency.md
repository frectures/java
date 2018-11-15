# Concurrency prior to Java 5

## Motivation

> Multicore processors are just now [May 2006] becoming inexpensive enough for midrange desktop systems.
> Not coincidentally, many development teams are noticing more and more threading-related bug reports in their projects.
> Dion Almaer, former editor of TheServerSide, recently blogged (after a painful debugging session that ultimately revealed a threading bug) that **most Java programs are so rife with concurrency bugs that they work only 'by accident'.** [JCIP]

## Concurrency primitives

* Every Java object has an associated monitor
* `synchronized` blocks, and methods (syntactic sugar)
* `wait` and `notify` methods for coordination
* `Thread` class
* `volatile` modifier

## Concurrency in frameworks

> It would be nice to believe that concurrency is an 'optional' or 'advanced' language feature, but the reality is that nearly all Java applications are multithreaded and these frameworks do not insulate you from the need to properly coordinate access to application state. [JCIP]

### Swing

Can you see how the folling Swing example is broken?

```java
public class GUI {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Close me!");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        final JButton button = new JButton("Click me to see the current date!");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                button.setText(new Date().toString());
            }
        });
        frame.add(button);

        frame.pack();
        frame.setVisible(true);
    }
}
```

The UI is put together in the main thread, but Swing requires most UI-related actions to happen in the [event dispatch thread](https://docs.oracle.com/javase/tutorial/uiswing/concurrency/dispatch.html):

```java
public class GUI {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Close me!");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            final JButton button = new JButton("Click me to see the current date!");
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    button.setText(new Date().toString());
                }
            });
            frame.add(button);

            frame.pack();
            frame.setVisible(true);
        });
    }
}
```

### Spring WebMVC

Can you see how the following Spring example is broken?

```java
@RestController
@RequestMapping("books")
public class BookController {

    private List<Book> books = new ArrayList<>();

    public BookController() {
        books.add(new Book("Effective Java", "Joshua Bloch", 2017));
        books.add(new Book("Java Concurrency in Practice", "Brian Goetz", 2006));
    }

    @GetMapping
    public Iterable<Book> getAllBooks() {
        return books;
    }

    @GetMapping("{index}")
    public Book getBookAt(@PathVariable("index") int index) {
        return books.get(index);
    }

    @PostMapping
    public void addBook(@RequestBody Book newBook) {
        books.add(newBook);
    }
}
```

Web requests are usually served by multiple threads, so **all** accesses to the list have to be synchronized:

```java
    @GetMapping
    public Iterable<Book> getAllBooks() {
        synchronized (books) {
            return books;
        }
    }

    @GetMapping("{index}")
    public Book getBookAt(@PathVariable("index") int index) {
        synchronized (books) {
            return books.get(index);
        }
    }

    @PostMapping
    public void addBook(@RequestBody Book newBook) {
        synchronized (books) {
            books.add(newBook);
        }
    }
```

> Whenever more than one thread accesses a given state variable, and one of them might write to it, they all must coordinate their access to it using synchronization.
> You should avoid the temptation to think that there are 'special' situations in which this rule does not apply. [JCIP]

## Goals of synchronization

Synchronization achieves 2 goals:
1. **Mutual exclusion:** Only one thread at a time can enter a synchronized block guarded by the same lock
2. **Visibility:** Changes made by one thread become visible to another thread synchronizing on the same lock

### Mutual exclusion

```java
public class UniqueIdGenerator {
    private static long id = 0L;
    
    public static long next() {
        return id++;
    }
}
```

What happens if two threads try to increment `id` at (roughly) the same time?

```
            Time --->
Thread #1   load (0)   increment (1)   store (1)
Thread #2   load (0)   increment (1)   store (1)
```

### Visibility

What happens if two threads try to increment `id` *after* one another?

```
            Time--->
Thread #1   load (0)   increment (1)   store (1)
Thread #2                                          load (0)   increment (1)   store (1)
                                                         ?
```

Without proper synchronization, Thread #2 could read either a 1 or a 0. Why does this happen?

1. `javac` optimizations
2. HotSpot optimizations
3. Instruction reordering
4. Cache hierarchies

```java
public class UniqueIdGenerator {
    private static long id = 0L;

    public static long next() {
        synchronized (UniqueIdGenerator.class) {
            return id++;
        }
    }
}
```

## Spin locks

Can you see how the following program is broken?

```java
public class Fred {
    public static void main(String[] args) {
        Fred fred = new Fred();
        new Thread(fred::calculate).start();
        fred.spin();
        fred.print();
    }

    private int[] squares;
    private boolean done;

    private void calculate() {
        squares = IntStream.range(0, 46341).map(x -> x * x).toArray();
        System.out.println("Squares calculated!");
        done = true;
    }

    private void spin() {
        int spinning = 0;
        while (!done) {
            ++spinning;
        }
        System.out.println("Spinlock span " + spinning + " times!");
    }

    private void print() {
        IntStream.of(squares).limit(11).forEach(System.out::println);
    }
}
```

The write to `done` and the read from `done` happen in different threads without synchronization.
It is impossible to predict whether or not the spinlock will ever terminate.

## volatile

The simplest solution is to mark `done` with `volatile`:

```java
    private volatile boolean done;
```

Writing to a `volatile` variable *v* from Thread #1
and then reading from the same `volatile` variable *v* from Thread #2
has the same visibility guarantees as leaving a synchronized block guarded by a lock *k* in Thread #1
and then entering a synchronized block guarded by the same lock *k* in Thread #2.

> A program that omits needed synchronization might appear to work, passing its tests and performing well for years, but it is still broken and may fail at any moment. [JCIP]

## join

Instead of actively waiting on a `boolean`, it is customary to `join` on the calculating `Thread`:

```java
        Fred fred = new Fred();
        Thread thread = new Thread(fred::calculate);
        thread.start();
        // ... other useful work ...
        thread.join();
        fred.print();
```

Of course, starting a thread and then *immediately* joining on it is a waste of resources,
as `join` is a blocking call.

## Coordination via `wait` and `notify`

> **Excercise:** Do you know how `wait` and `notify` work?
> Then implement the following `ThreadPool` class.
> Otherwise, let us do it together!

```java
public class ThreadPool {
    private Thread[] threads;
    private Deque<Runnable> jobs;

    public ThreadPool(int numThreads) {
        // ...
    }

    public void execute(Runnable job) {
        // ...
    }
}
```

# Concurrency since Java 5

The `java.util.concurrent` package provides higher-level concurrency tools.

## Atomic types

```java
public class UniqueIdGenerator {
    private static long id = 0L;

    public static long next() {
        synchronized (UniqueIdGenerator.class) {
            return id++;
        }
    }
}
```

Synchronized access to a `long` variable can be replaced with an `AtomicLong`:

```java
public class UniqueIdGenerator {
    private static AtomicLong id = new AtomicLong(0L);

    public static long next() {
        return id.getAndIncrement();
    }
}
```

* Implemented with special hardware instructions
  * No locking required
  * Faster than synchronized
* Other popular atomic types:
  * `AtomicInt`
  * `AtomicBoolean`
  * `AtomicReference`

*demonstrate WorldPanel*

## Collections

### Synchronized collections

* vintage `java.util.Vector`
* `Collections.synchronizedXxx`, popular variants:
  * `synchronizedCollection`
  * `synchronizedList`
  * `synchronizedSet`
  * `synchronizedMap`

Note that `synchronizedList` cannot be used in our `BookController` example, because Jackson iteration is out of our control:

> It is imperative that the user manually synchronize on the returned list when traversing it via Iterator.
> Failure to follow this advice may result in non-deterministic behavior.

### Copy-on-write collections

* `CopyOnWriteArrayList`
* `CopyOnWriteArraySet`

```java
    private CopyOnWriteArrayList<Book> books = new CopyOnWriteArrayList<>();

    @GetMapping
    public Iterable<Book> getAllBooks() {
        return books;
    }

    @GetMapping("{index}")
    public Book getBookAt(@PathVariable("index") int index) {
        return books.get(index);
    }

    @PostMapping
    public void addBook(@RequestBody Book newBook) {
        books.add(newBook);
    }
```

Probably not the right choice for our use case, unless book additions are rare:

> A thread-safe variant of `ArrayList` in which all mutative operations are implemented by making a fresh copy of the underlying array.
>
> This is ordinarily too costly, but may be more efficient than alternatives when traversal operations vastly outnumber mutations,
> and is useful when you cannot or don't want to synchronize traversals, yet need to preclude interference among concurrent threads.

### Persistent collections

A better alternative to copy-on-write collections are "persistent collections" as pioneered by Clojure:

```java
import io.vavr.collection.Vector;

@RestController
@RequestMapping("books")
public class BookController {

    public BookController() {
        Vector<Book> books = Vector.of(
                new Book("Effective Java", "Joshua Bloch", 2017),
                new Book("Java Concurrency in Practice", "Brian Goetz", 2006));
        atomicBooks = new AtomicReference<>(books);
    }

    private AtomicReference<Vector<Book>> atomicBooks;

    @GetMapping
    public Iterable<Book> getAllBooks() {
        return atomicBooks.get().asJava();
    }

    @GetMapping("{index}")
    public Book getBookAt(@PathVariable("index") int index) {
        return atomicBooks.get().get(index);
    }

    @PostMapping
    public void addBook(@RequestBody Book newBook) {
        atomicBooks.updateAndGet(books -> books.append(newBook));
    }
}
```

Persistent vectors are immutable but offer "effectively constant time" append performance.

### Concurrent collections

* `ConcurrentHashMap`
* `ConcurrentLinkedQueue`
* `ConcurrentLinkedDeque`
* `ConcurrentSkipListMap`
* `ConcurrentSkipListSet`

`ConcurrentHashMap` introduced 2 interesting operations which were adopted by `Map`:

```java
/**
 * @return the previous value associated with the specified key,
 * or null if there was no mapping for the key
 */
default V putIfAbsent(K key, V value) {
    V v = get(key);
    if (v == null) {
        v = put(key, value);
    }
    return v;
}

/**
 * @return the current (existing or computed) value associated with the specified key,
 * or null if the computed value is null
 */
default V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
    Objects.requireNonNull(mappingFunction);
    V v;
    if ((v = get(key)) == null) {
        V newValue;
        if ((newValue = mappingFunction.apply(key)) != null) {
            put(key, newValue);
            return newValue;
        }
    }
    return v;
}
```

> **Exercise:** Write a program that counts the occurrences of words in a large (!) text file.
> Measure whether parallel streams result in a significant performance difference for this use case.
>
> **Hint:** The method `java.nio.file.Files.lines` returns a `Stream<String>` containing the lines.

### Blocking queues

* `BlockingQueue`
  * `ArrayBlockingQueue`
  * `LinkedBlockingQueue`
  * `LinkedBlockingDeque`
* `BlockingDeque`
  * `LinkedBlockingDeque`

> **Exercise:** Simplify our `ThreadPool` class by replacing the `Deque` with a `BlockingDeque`.
> The calls to `wait` and `notify` should vanish.

## Task execution

Our custom `ThreadPool` class can be completely replaced with an `ExecutorService`:

```java
ExecutorService fixed = Executors.newFixedThreadPool(8);
fixed.execute(myRunnable);

ExecutorService cached = Executors.newCachedThreadPool();

ExecutorService single = Executors.newSingleThreadExecutor();
```

### The Future is looking bright

* `void ExecutorService.execute(Runnable command)` is fire-and-forget
* `Future<T> ExecutorService.submit(Callable<T> task)` returns a `Future` than can be queried for the result later
* `Future<?> ExecutorService.submit(Runnable task)` can at least be queried for completion

*demonstrate LabyrinthGenerator*

### Java 8 makes the Future complete

* A `Future` must be actively queried for completion (pull)
* A `CompleteableFuture` can trigger a callback on completion (push)

```java
CompletableFuture.supplyAsync(Math::random).thenAccept(System.out::println);
```

## Explicit locks

* `synchronized` blocks implicitly lock and unlock
* Explicit locks offer finer-grained control over locking behavior

### ReadWriteLock

A `ReadWriteLock` can be locked by one writer or *multiple* readers:

```java
@RestController
@RequestMapping("books")
public class BookController {

    private List<Book> books = new ArrayList<>();
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    public BookController() {
        books.add(new Book("Effective Java", "Joshua Bloch", 2017));
        books.add(new Book("Java Concurrency in Practice", "Brian Goetz", 2006));
    }

    @GetMapping
    public Iterable<Book> getAllBooks() {
        readWriteLock.readLock().lock();
        try {
            return books;
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    @GetMapping("{index}")
    public Book getBookAt(@PathVariable("index") int index) {
        readWriteLock.readLock().lock();
        try {
            return books.get(index);
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    @PostMapping
    public void addBook(@RequestBody Book newBook) {
        readWriteLock.writeLock().lock();
        try {
            books.add(newBook);
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }
}
```
