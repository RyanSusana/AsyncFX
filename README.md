# About AsyncFX
AsyncFX is a library for Java and Kotlin aimed at simplifying the development of asynchronous calls in JavaFX.


# Basic Usage

## Java
```java
AsyncTasks.newTask(Integer.class, String.class)

//Happens before async call and is blocking on the JavaFX thread.
.before(() -> System.out.println("This will be  before"))

//Happens in a separate non JavaFX Thread, takes input(from .execute(inputParams))
.inBackground(inputIntegerArray -> {

    //inputIntegerArray comes from .execute(Integer... inputIntegerArray) call
    long randomInt = new Random().nextInt(inputIntegerArray[0] * inputIntegerArray[1]);

    Thread.sleep(randomInt);
    return randomInt + "ms";
})

//After the inBackground call. Runs in a JavaFX thread.
.after(result -> System.out.println(String.format("Background process ran in %s", result)))

//Execute the task here you can provide optional input to the inBackground call
.execute(100, 10)

//Call this to block until this task ends
.andWait();

```

## Kotlin

```kotlin
AsyncTasks.newTask<Int, String>()

//Happens before async call and is blocking on the JavaFX thread.
.before { println("This will be  before") }

//Happens in a separate non JavaFX Thread, takes input(from .execute(inputParams))
.inBackground { inputIntegerArray ->

    //inputIntegerArray comes from .execute(Integer... inputIntegerArray) call
    val randomInt = Random().nextInt(inputIntegerArray[0] * inputIntegerArray[1]).toLong()

    Thread.sleep(randomInt)
    randomInt.toString() + "ms"
}

//After the inBackground call. Runs in a JavaFX thread.
.after { result -> println("Background process ran in %s".format(result)) }

//Execute the task here you can provide optional input to the inBackground call
.execute(100, 10)

//Call this to block until this task ends
.andWait()
```