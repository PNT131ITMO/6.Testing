package lab1.part3.models;

import lab1.part3.enums.SoundStage;

public class Engine implements Validatable {
    private SoundStage stage;

    public Engine(SoundStage initialStage) {
        this.stage = initialStage;
        validate();
    }

    public SoundStage getStage() { return stage; }

    public SoundStage advance() {
        switch (stage) {
            case BUZZ -> stage = SoundStage.WHISTLE;
            case WHISTLE -> stage = SoundStage.ROAR;
            case ROAR -> stage = SoundStage.ROAR;
        }
        return stage;
    }

    @Override
    public void validate() {
        if (stage == null) throw new IllegalArgumentException("Engine.stage must not be null");
    }
}
