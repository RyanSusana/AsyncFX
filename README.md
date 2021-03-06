![Travis Button](https://travis-ci.org/RyanSusana/AsyncFX.svg?branch=master)
[![codebeat badge](https://codebeat.co/badges/daf90772-501e-482f-889e-f7af48de6fa4)](https://codebeat.co/projects/github-com-ryansusana-asyncfx-master)

# About AsyncFX
AsyncFX is a library for Java and Kotlin aimed at simplifying the development of asynchronous calls in JavaFX.

# Download
```xml
<dependency>
    <groupId>com.ryansusana.asyncfx</groupId>
    <artifactId>asyncfx</artifactId>
    <version>0.0.4</version>
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

//Execute the task with input
.execute(10, 100)

//Block the current thread until the task has been executed
.andWait();

```

## Kotlin
This is the Kotlin equivalent to the above Java code.
```kotlin
AsyncTasks.newTypedTask<Int, String>()

//Happens before async call and is blocking on the JavaFX thread.
.before { println("This will be executed before") }

//Happens in a separate non JavaFX Thread, takes input(from .execute(inputParams))
.inBackground { inputIntegerArray ->

    //inputIntegerArray comes from .execute(Integer... inputIntegerArray) call
    val randomInt = Random().nextInt(inputIntegerArray[0] * inputIntegerArray[1]).toLong()

    Thread.sleep(randomInt)
    randomInt.toString() + "ms"
}

//After the inBackground call. Runs in a JavaFX thread.
.after { result -> println("Background process ran in %s".format(result)) }

//Execute the task with input
.execute(10, 100)

//Block the current thread until the task has been executed
.andWait()
```

## Kotlin (simplified)
In version 0.0.2 the new Kotlin Builder API came out. It significantly improved the creation of Kotlin Tasks.
```kotlin
typedTask<Int, String> {
    before { println("This will be executed before") }

    inBackground { inputIntegerArray ->
        val randomInt = Random().nextInt(inputIntegerArray[0] * inputIntegerArray[1]).toLong()
        Thread.sleep(randomInt)
        randomInt.toString() + "ms"
    }
    after { result -> println("Background process ran in %s".format(result)) }
}.execute().andWait()
```

# Pools
In version 0.0.3 Pools were introduced. Pools are an easy way to execute multiple tasks asynchronously.

## Pools in Kotlin
```kotlin
pool {
    //A basic task is a task with just the inBackground specified.
    for (i in 1..5) {
        basicTask {
            Thread.sleep(i * 100L)
            //Intensive task, executed on a separate non-JavaFX Thread
        }
    }
    
    task {
        before {
            //Before the task, executed on the JavaFX thread
        }
        inBackground {
            Thread.sleep(400)
           //Some intensive task on another non-JavaFX thread
        }
        after {
            //After the task, executed on the JavaFX thread
        }
    }
}.execute().andWait()
```

## Pools in Java
```java
AsyncTaskPool<Integer, String> pool = AsyncTasks.newTypedPool();

for (int i = 0; i < 20; i++) {
    final int finalI = i;
    pool.addTask(AsyncTasks.newTypedTask(Integer.class, String.class)
            .before(() -> System.out.println("This will be executed before"))

            .inBackground(inputIntegerArray -> {

                long sleepTime = inputIntegerArray[0] * finalI;

                Thread.sleep(sleepTime);
                return sleepTime + "ms";
            })
            .after(result -> System.out.println(String.format("Background process ran in %s", result)))

    );
}
//The 20 goes into the inBackground params
pool.execute(20).andWait();
//Each subsequent task will run for 20ms longer than the previous
```
