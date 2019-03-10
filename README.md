![Travis Button](https://travis-ci.org/RyanSusana/AsyncFX.svg?branch=master)

# About AsyncFX
AsyncFX is a library for Java and Kotlin aimed at simplifying the development of asynchronous calls in JavaFX.

# Download
```xml
<dependency>
    <groupId>com.ryansusana.asyncfx</groupId>
    <artifactId>asyncfx</artifactId>
    <version>0.0.1</version>
</dependency>
```
# Basic Usage

## Java
```java
AsyncTasks.newTypedTask(Integer.class, String.class)

//Happens before async call and is blocking on the JavaFX thread.
.before(() -> System.out.println("This will be executed before"))

//Happens in a separate non JavaFX Thread, takes input(from .execute(inputParams))
.inBackground(inputIntegerArray -> {

    //inputIntegerArray comes from .execute(Integer... inputIntegerArray) call
    long randomInt = new Random().nextInt(inputIntegerArray[0] * inputIntegerArray[1]);

    Thread.sleep(randomInt);
    return randomInt + "ms";
})

//After the inBackground call. Runs in a JavaFX thread.
.after(result -> System.out.println(String.format("Background process ran in %s", result)))

typedTask
.execute(10, 100)

typedTask
.andWait();

```

## Kotlin

```kotlin
AsyncTasks.newTypedTask<Int, String>()

//Happens before async call and is blocking on the JavaFX thread.
.before { println("This will be executed before") }

//Happens in a separate non JavaFX Thread, takes input(from .execute(inputParams))
.inBackground { inputIntegerArray ->

    //inputIntegerArray comes from .execute(Integer... inputIntegerArray) call
    val randomInt = Random().nextInt(inputIntegerArray[0] * inputIntegerArray[1])

    Thread.sleep(randomInt)
    randomInt.toString() + "ms"
}

//After the inBackground call. Runs in a JavaFX thread.
.after { result -> println("Background process ran in %s".format(result)) }

typedTask
.execute(10, 100)

typedTask
.andWait()
```
