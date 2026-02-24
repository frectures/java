## Case study: GUI code

### Anonymous inner classes

```java
void main() {
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
    frame.show();
}
```

- `GUI.java`
  - `GUI.class`
  - `GUI$1.class`

### Lambdas

Lambdas are more concise than anonymous inner classes:

```java
    button.addActionListener((ActionEvent event) -> {
        button.setText(new Date().toString());
    });
```

Lambda parameter types can be inferred by the compiler:

```java
    button.addActionListener((event) -> {
        button.setText(new Date().toString());
    });
```

Parentheses around a single lambda parameter name are optional:

```java
    button.addActionListener(event -> {
        button.setText(new Date().toString());
    });
```

The curly braces around a single statement are optional:

```java
    button.addActionListener(event -> button.setText(new Date().toString()));
```

- `GUI.java`
  - `GUI.class`
- 📺 [Lambdas in Java: A Peek under the Hood](https://www.youtube.com/watch?v=MLksirK9nnE)
