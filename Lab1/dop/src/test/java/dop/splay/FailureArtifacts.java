package dop.splay;

import net.jqwik.api.stateful.ActionSequence;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

final class FailureArtifacts {

    static void writeFailureReport(ActionSequence<World> sequence, World world, Throwable error) {
        String dirFromGradle = System.getProperty("statefulArtifactDir");
        if (dirFromGradle == null || dirFromGradle.isBlank()) {
            throw new IllegalStateException(
                "System property 'statefulArtifactDir' is not set. " +
                "Configure it in build.gradle test task."
            );
        }

        Path outDir = Path.of(dirFromGradle);
        Path outFile = outDir.resolve("minimal-failing-sequence.txt");

        try {
            Files.createDirectories(outDir);

            StringBuilder report = new StringBuilder();
            report.append("Sequence reported by jqwik:\n");
            report.append(sequence).append("\n\n");
            report.append("Error type: ").append(error.getClass().getName()).append("\n");
            report.append("Error message: ").append(error.getMessage()).append("\n\n");
            report.append("Executed steps before failure: ").append(world.stepCount()).append("\n\n");
            report.append("Last structural snapshot:\n");
            report.append(world.lastSnapshot().toArtifactText()).append("\n");

            Files.writeString(outFile, report.toString(), StandardCharsets.UTF_8);

            System.err.println("Failure artifact written to: " + outFile.toAbsolutePath());
        } catch (IOException ioError) {
            throw new RuntimeException(
                "Failed to write stateful testing artifact to: " + outFile.toAbsolutePath(),
                ioError
            );
        }
    }

    private FailureArtifacts() {}
}