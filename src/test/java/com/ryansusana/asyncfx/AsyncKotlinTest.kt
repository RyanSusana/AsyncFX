package com.ryansusana.asyncfx

import org.junit.jupiter.api.Test
import java.util.*

class AsyncKotlinTest {

    @Test
    internal fun testSyntaxWithComments() {
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
    }

    @Test
    internal fun testSyntaxWithoutComments() {
        AsyncTasks.newTask<Int, String>()
                .before { println("This will be  before") }
                .inBackground { inputIntegerArray ->

                    val randomInt = Random().nextInt(inputIntegerArray[0] * inputIntegerArray[1]).toLong()

                    Thread.sleep(randomInt)
                    randomInt.toString() + "ms"
                }
                .after { result -> println("Background process ran in %s".format(result)) }
                .execute(100, 10)
                .andWait()
    }
}