package com.ryansusana.asyncfx;

import javafx.embed.swing.JFXPanel;
import org.junit.jupiter.api.Test;

import java.util.Random;

public class AsyncTest {

    @Test
    void testSyntax() throws InterruptedException {

        new JFXPanel();

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
    }
}
