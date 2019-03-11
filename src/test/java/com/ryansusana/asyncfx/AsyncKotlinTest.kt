package com.ryansusana.asyncfx

import com.ryansusana.asyncfx.AsyncTasks.newPool
import com.ryansusana.asyncfx.AsyncTasks.newTypedTask
import org.junit.jupiter.api.Test
import org.testfx.api.FxToolkit
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class AsyncKotlinTest {

    @Test
    internal fun testSyntaxWithComments() {
        FxToolkit.registerPrimaryStage()


        newTypedTask<Int, String>()

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

                //Execute the typedTask here you can provide optional input to the inBackground call
                .execute(100, 10)

                //Call this to block until this typedTask ends
                .andWait()
    }

    @Test
    internal fun testSyntaxWithoutComments() {
        FxToolkit.registerPrimaryStage()
        newTypedTask<Int, String>()

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
        FxToolkit.registerPrimaryStage()
        val atomicBoolean = AtomicBoolean(false)

        newTypedTask<Int, Boolean>()
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
        FxToolkit.registerPrimaryStage()
        val atomicBoolean = AtomicBoolean(false)

        newTypedTask<Int, Boolean>()
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

    @Test
    internal fun testAsyncPool() {
        FxToolkit.registerPrimaryStage()

        val asyncTaskPool = newPool()
        val atomicInteger = AtomicInteger(0)
        val amountOfTasks = 20
        val durationPerTask = 100L


        for (i in 1..amountOfTasks) {
            asyncTaskPool.addTask(
                    task {
                        inBackground {
                            Thread.sleep(durationPerTask)
                            atomicInteger.getAndAdd(1)
                        }
                    }
            )

        }

        // It needs to execute in at least half of durationPerTask * amountOfTasks.
        // This ensures that tasks are running at the time.
        asyncTaskPool.execute().andWaitFor((amountOfTasks / 2) * durationPerTask, TimeUnit.MILLISECONDS)

        assertEquals(amountOfTasks, atomicInteger.get())

    }

    @Test
    internal fun testCoolSyntax() {
        FxToolkit.registerPrimaryStage()

        val typedTask = typedTask<Int, String> {
            before { println("This will be executed before") }

            inBackground { inputIntegerArray ->
                val randomInt = Random().nextInt(inputIntegerArray[0] * inputIntegerArray[1]).toLong()
                Thread.sleep(randomInt)
                randomInt.toString() + "ms"
            }
            after { result -> println("Background process ran in %s".format(result)) }
        }

        typedTask.execute(10, 100).andWait()
    }

    @Test
    internal fun testPoolCoolSyntax() {
        FxToolkit.registerPrimaryStage()
        val atomicInteger = AtomicInteger(0)
        val pool =

                pool {
                    basicTask {
                        Thread.sleep(100)
                        atomicInteger.getAndAdd(1)
                    }
                    basicTask {
                        Thread.sleep(200)
                        atomicInteger.getAndAdd(1)
                    }
                    basicTask {
                        Thread.sleep(300)
                        atomicInteger.getAndAdd(1)
                    }
                    task {
                        before {

                        }
                        inBackground {

                            Thread.sleep(400)
                            atomicInteger.getAndAdd(1)
                        }
                        after {

                        }
                    }
                    typedTask {

                        inBackground {
                            Thread.sleep(500)
                            atomicInteger.getAndAdd(1)
                        }
                    }

                }.execute()
        assertNotEquals(3, atomicInteger.get())

        pool.andWaitFor(600, TimeUnit.MILLISECONDS)
        assertEquals(5, atomicInteger.get())

    }
}