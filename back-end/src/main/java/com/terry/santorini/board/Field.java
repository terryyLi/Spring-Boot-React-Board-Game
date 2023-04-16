package com.terry.santorini.board;

public final class Field {
    private final int fieldId;
    private final int level;
    private final boolean isCapped;
    private final Worker occupant;

    private static final int MAX_LEVEL = 3;

    public Field(int fieldId) {
        this.fieldId = fieldId;
        this.level = 0;
        this.isCapped = false;
        this.occupant = null;
    }

    public Field(int fieldId, int level, boolean isCapped, Worker occupant) {
        this.fieldId = fieldId;
        this.level = level;
        this.isCapped = isCapped;
        this.occupant = occupant;
    }

    /**
     * Increase tower level by 1, but not exceeding the maximum level
     * 
     * @return new {@link Field} with updated level 
     */
    public Field buildBlock() {
        int newLevel = Math.min(level+1, MAX_LEVEL);
        return new Field(fieldId, newLevel, isCapped, occupant);
    }

    /**
     * Cap the field with a dome, regardless of the level
     * If it was already capped, this has no effect
     * 
     * @return updated {@link Field}
     */
    public Field buildDome() {
        return new Field(fieldId, level, true, occupant);
    }

    /**
     * Occupy the field using a worker
     * If it was already occupied, replace the original occupant
     * 
     * @param worker {@link Worker} who is occupying this space
     * 
     * @return updated {@link Field}
     */
    public Field enter(Worker worker) {
        return new Field(fieldId, level, isCapped, worker);
    }

    /**
     * Vacate the field's occupant
     * 
     * @return updated {@link Field}
     */
    public Field leave() {
        return new Field(fieldId, level, isCapped, null);
    }

    /**
     * Check whether the field is capped by a dome
     * 
     * @return true if it is capped
     */
    public boolean isCapped () {
        return this.isCapped;
    }

    /**
     * Check whether the tower is fully constructed
     * 
     * @return true if it reaches the maximum level
     */
    public boolean isFullBuild() {
        return this.level >= MAX_LEVEL;
    }

    /**
     * Check whether the field is occupied by a worker
     * 
     * @return true if it is occupied
     */
    public boolean isOccupied () {
        return this.occupant != null;
    }

    /**
     * Retrieve the level of the current field
     * 
     * @return integer between 0 and 3
     */
    public int getLevel() {
        return this.level;
    }

    /**
     * Retrieve the occupant of the current field
     * 
     * @return {@link Worker}
     */
    public Worker getOccupant () {
        return this.occupant;
    }

    @Override
    public String toString() {
        String workerState;
        if (this.isOccupied()) {
            workerState = this.occupant.toString();
        }
        else {
            workerState = (new Worker(-1, "")).toString();
        }
        return """
                {
                    "field_id": %d,
                    "level": %d,
                    "isCapped": %b,
                    "hasWorker": %b,
                    "worker": %s
                }
                """.formatted(this.fieldId, this.level, 
                        this.isCapped(),
                        this.isOccupied(), 
                        workerState);
    } 
}

