package com.ryansusana.asyncfx

import com.ryansusana.asyncfx.AsyncTasks.newTask
import javafx.embed.swing.JFXPanel
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AsyncKotlinTest {


    @BeforeAll
    internal fun setUp() {

    }


    @Test
    internal fun testSyntaxWithComments() {
        JFXPanel()
        newTask<Int, String>()

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

                //Execute the task here you can provide optional input to the inBackground call
                .execute(100, 10)

                //Call this to block until this task ends
                .andWait()
    }

    @Test
    internal fun testSyntaxWithoutComments() {
        JFXPanel()
        newTask<Int, String>()
                .before { println("This will be executed before") }
                .inBackground { inputIntegerArray ->

                    val randomInt = Random().nextInt(inputIntegerArray[0] * inputIntegerArray[1]).toLong()

                    Thread.sleep(randomInt)
                    randomInt.toString() + "ms"
                }
                .after { result -> println("Background process ran in %s".format(result)) }
                .execute(100, 10)
                .andWait()
    }

    @Test
    internal fun testNonBlocking() {
        JFXPanel()
        val atomicBoolean = AtomicBoolean(false)

        newTask<Int, Boolean>()
                .inBackground {
                    Thread.sleep(200)
                    true
                }

                .after { result ->
                    atomicBoolean.set(result)
                }

                .execute()

        assertFalse { atomicBoolean.get() }
    }

    @Test
    internal fun testWaitOnResult() {
        JFXPanel()
        val atomicBoolean = AtomicBoolean(false)

        newTask<Int, Boolean>()
                .inBackground {
                    Thread.sleep(200)
                    true
                }

                .after { result ->
                    atomicBoolean.set(result)
                }
                .execute()
                .andWait()

        assertTrue { atomicBoolean.get() }
    }
}