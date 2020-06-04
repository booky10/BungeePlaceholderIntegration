package tk.t11e.bpi.bungee;
// Created by booky10 in BungeePlaceholderIntegration (19:06 04.06.20)

import java.util.ArrayList;
import java.util.List;

public class CompletableInFuture<T> {

    private final List<Output<T>> outputs = new ArrayList<>();

    public void getValue(Output<T> output) {
        outputs.add(output);
    }

    public void setValue(T value) {
        for (Output<T> output : outputs)
            output.onValueSet(value);
        outputs.clear();
    }

    public interface Output<T> {

        void onValueSet(T value);
    }
}