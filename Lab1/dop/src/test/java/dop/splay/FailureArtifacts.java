package dop.splay;

import net.jqwik.api.stateful.ActionSequence;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

final class FailureArtifacts {
    
    static void writeMinimalFailingSequence(ActionSequence<World> sequence, Throwable error) {
        try {
            Path outDir = Paths.get("build", "stateful-artifacts");
            Files.createDirectories(outDir);

            String content = "Minimal failing sequence:" + sequence;

            Files.writeString(
                outDir.resolve("minimal-failing-sequence.txt"), content, StandardCharsets.UTF_8);
        } catch (Exception ignored) {}
    }

    private FailureArtifacts() {}
}
