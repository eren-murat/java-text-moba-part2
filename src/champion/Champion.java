package champion;

import angel.Angel;
import observer.Observer;
import util.Constants;

import java.util.ArrayList;

public abstract class Champion {
    private char preferredTerrain;
    private float terrainModifier;
    private int hp;
    private int hpStart;
    private int hpGrowth;
    private int hpBeforeRound;
    private int xp;
    private int level;
    private boolean applyTerrainModifier;
    private ArrayList<Integer> damageOverTime = new ArrayList<Integer>();
    private int posX;
    private int posY;
    private int id;
    private int incapacitated;
    private boolean foughtThisRound;
    private float firstAbilityBase;
    private float firstAbilityGrowth;
    private float secondAbilityBase;
    private float secondAbilityGrowth;
    private float raceModWizardFirst;
    private float raceModWizardSecond;
    private float raceModKnightFirst;
    private float raceModKnightSecond;
    private float raceModPyromancerFirst;
    private float raceModPyromancerSecond;
    private float raceModRogueFirst;
    private float raceModRogueSecond;
    private Observer observer;
    /**
     *  Method is used to register an observer.
     * @param newObserver observer to be added.
     */
    public void addObserver(final Observer newObserver) {
        observer = newObserver;
    }

    /**
     *  Method is used to notify the observer when a kill occurs.
     * @param killer champion that killed
     */
    public void notifyKill(final Champion killer) {
        observer.updateKill(this, killer);
    }

    /**
     *  Method is used to notify the observer when the champion levels up.
     */
    private void notifyLevelUp() {
        observer.updateLevelUp(this);
    }

    /**
     *  Method is used to move the champion.
     * @param newMove represents the direction of the move
     */
    public void makeMove(final char newMove) {
        switch (newMove) {
            case 'U':
                --posX;
                break;
            case 'D':
                ++posX;
                break;
            case 'R':
                ++posY;
                break;
            case 'L':
                --posY;
                break;
            case '_':
                break;
            default:
                break;
        }
    }

    /**
     *  Method is used to display the final status of a champion.
     * @return champion stats if he is alive, or it will display "dead"
     */
    public String printFinalStats() {
        if (isAlive()) {
            return (getName() + " " + level + " " + xp + " " + hp + " "
                    + posX + " " + posY + "\n");
        } else {
            return (getName() + " dead\n");
        }
    }

    /**
     *  Method is used to determine if champion is a valid opponent.
     * @param champion opponent that "this" will fight
     * @return true if the fight can take place, false otherwise
     */
    public boolean verifyOpponent(final Champion champion) {
        return this.posX == champion.getPosX() && this.posY == champion.getPosY()
                && this.id != champion.getID() && !this.foughtThisRound
                && !champion.getFoughtThisRound();
    }

    public final int calculateLevelUpLimit() {
        return Constants.LEVEL_UP_XP + Constants.LEVEL_UP_XP_GROWTH * getLevel();
    }

    /**
     * Method is used to award XP to the winner after a fight and level him up, if
     * necessary.
     * @param levelLoser level of the champion that died after the fight
     * @return true if the winner accumulated enough XP to level up, false if otherwise
     */
    public boolean awardXP(final int levelLoser) {
        int levelDiff = getLevel() - levelLoser;
        int xpWinner = Math.max(0, Constants.XP_INDICATOR - levelDiff * Constants.XP_MULTIPLIER);
        xp += xpWinner;

        boolean leveledUp = false;
        while (xp >= calculateLevelUpLimit()) {
            ++level;
            notifyLevelUp();
            leveledUp = true;
        }
        return leveledUp;
    }

    /**
     *  Method is used to test if the champion is alive or dead.
     * @return true if the champion is alive, false if he is dead
     */
    public boolean isAlive() {
        return hp > 0;
    }

    /**
     *  Method is used to determine if the champion is incapacitated.
     *  The field "incapacitated" stores the number of rounds he will be under the effect.
     * @return true if the champion is under an incapacitation effect, false otherwise
     */
    public boolean isIncapacitated() {
        if (incapacitated > 0) {
            --incapacitated;
            return true;
        }
        return false;
    }

    /**
     *  Method is used to apply the effect of the abilities that have a damage over time
     *  (DOT - how is called in MOBAs) effect.
     */
    public void applyDamageOverTime() {
        if (damageOverTime.size() > 0) {
            reduceHP(damageOverTime.get(0));
            damageOverTime.remove(0);
        }
    }

    /**
     *  Method is used to store the damage over time from the specific abilities.
     * @param damage damage to be taken over time
     * @param rounds the number of rounds the DOT effect will be active
     */
    final void addDamageOverTime(final int damage, final int rounds) {
        for (int i = 0; i < rounds; ++i) {
            damageOverTime.add(damage);
        }
    }

    /**
     *  Method is used to overwrite the current damage over time effects when the champion is hit
     *  with a new ability that has a damage over time effect.
     */
    final void resetDamageOverTime() {
        damageOverTime.clear();
    }

    /**
     *  Method is used to determine if the terrain the champion is sitting on will give him
     *  a bonus.
     * @param terrainType the type of the current location of the champion
     */
    public void hasTerrainModifier(final char terrainType) {
        applyTerrainModifier = preferredTerrain == terrainType;
    }

    /**
     *  Method is used to determine the maximum possible HP at a given point.
     * @return maximum possible HP
     */
    public final int calculateTheoreticalHP() {
        return hpStart + hpGrowth * level;
    }
    /**
     *  Method is used to determine the initial of a champion by its class.
     * @return  char representing champion type
     */
    private char getName() {
        return getClass().getName().charAt(Constants.NAME_INDEX);
    }

    public final  String getFullName() {
        return getClass().getName().substring(getClass().getName().lastIndexOf(".") + 1);
    }

    final ArrayList<Integer> getDamageOverTime() {
        return damageOverTime;
    }

    public final boolean getApplyTerrainModifier() {
        return applyTerrainModifier;
    }

    final boolean getFoughtThisRound() {
        return foughtThisRound;
    }

    /**
     *  Method is used to set the status of the champion involvement in a round.
     * @param fightStatus true if the champion fought, false if otherwise
     */
    public void setFoughtThisRound(final boolean fightStatus) {
        foughtThisRound = fightStatus;
    }

    /**
     *  Method is a setter for incapacitated field that stores the number of rounds
     *  the champion is under the incapacitation effect.
     * @param incapacitated number of incapacitation rounds
     */
    final void setIncapacitated(final int incapacitated) {
        this.incapacitated = incapacitated;
    }

    public final void addXP(final int newXP) {
        xp += newXP;
    }

    public final int getXP() {
        return xp;
    }

    final void setXP() {
        xp = 0;
    }

    public final int getHP() {
        return hp;
    }

    public final void setHP(final int newHP) {
        this.hp = newHP;
    }

    final void setHPGrowth(final int newHPGrowth) {
        this.hpGrowth = newHPGrowth;
    }

    final void setHPStart(final int newHPStart) {
        this.hpStart = newHPStart;
    }

    final int getHPBeforeRound() {
        return hpBeforeRound;
    }

    public final void setHPBeforeRound(final int newHP) {
        hpBeforeRound = newHP;
    }

    public final int getLevel() {
        return level;
    }

    final void setLevel() {
        this.level = 0;
    }

    public final void increaseLevel() {
        ++level;
    }

    public final int getPosX() {
        return posX;
    }

    final void setPosX(final int posX) {
        this.posX = posX;
    }

    public final int getPosY() {
        return posY;
    }

    final void setPosY(final int posY) {
        this.posY = posY;
    }

    public final int getID() {
        return id;
    }

    public final void setID(final int newId) {
        id = newId;
    }

    final void setPreferredTerrain(final char preferredTerrain) {
        this.preferredTerrain = preferredTerrain;
    }

    final float getTerrainModifier() {
        return terrainModifier;
    }

    final void setTerrainModifier(final float terrainModifier) {
        this.terrainModifier = terrainModifier;
    }

    final void setFirstAbilityBase(final float firstAbilityBase) {
        this.firstAbilityBase = firstAbilityBase;
    }

    final void setFirstAbilityGrowth(final float firstAbilityGrowth) {
        this.firstAbilityGrowth = firstAbilityGrowth;
    }

    final void setSecondAbilityBase(final float secondAbilityBase) {
        this.secondAbilityBase = secondAbilityBase;
    }

    final void setSecondAbilityGrowth(final float secondAbilityGrowth) {
        this.secondAbilityGrowth = secondAbilityGrowth;
    }

    final float getRaceModWizardFirst() {
        return raceModWizardFirst;
    }

    final void setRaceModWizardFirst(final float raceModWizardFirst) {
        this.raceModWizardFirst = raceModWizardFirst;
    }

    final float getRaceModWizardSecond() {
        return raceModWizardSecond;
    }

    final void setRaceModWizardSecond(final float raceModWizardSecond) {
        this.raceModWizardSecond = raceModWizardSecond;
    }

    final float getRaceModKnightFirst() {
        return raceModKnightFirst;
    }

    final void setRaceModKnightFirst(final float raceModKnightFirst) {
        this.raceModKnightFirst = raceModKnightFirst;
    }

    final float getRaceModKnightSecond() {
        return raceModKnightSecond;
    }

    final void setRaceModKnightSecond(final float raceModKnightSecond) {
        this.raceModKnightSecond = raceModKnightSecond;
    }

    final float getRaceModPyromancerFirst() {
        return raceModPyromancerFirst;
    }

    final void setRaceModPyromancerFirst(final float raceModPyromancerFirst) {
        this.raceModPyromancerFirst = raceModPyromancerFirst;
    }

    final float getRaceModPyromancerSecond() {
        return raceModPyromancerSecond;
    }

    final void setRaceModPyromancerSecond(final float raceModPyromancerSecond) {
        this.raceModPyromancerSecond = raceModPyromancerSecond;
    }

    final float getRaceModRogueFirst() {
        return raceModRogueFirst;
    }

    final void setRaceModRogueFirst(final float raceModRogueFirst) {
        this.raceModRogueFirst = raceModRogueFirst;
    }

    final float getRaceModRogueSecond() {
        return raceModRogueSecond;
    }

    final void setRaceModRogueSecond(final float raceModRogueSecond) {
        this.raceModRogueSecond = raceModRogueSecond;
    }

    /**
     *  Methos is used to calculate the base damage for the first ability.
     * @return base damage of first ability
     */
    final float firstAbility() {
        return firstAbilityBase + firstAbilityGrowth * level;
    }

    /**
     *  Method is used to calculate the base damage for the second ability.
     * @return  base damage of second ability
     */
    final float secondAbility() {
        return secondAbilityBase + secondAbilityGrowth * level;
    }

    /**
     *  Method is used to lower the hp after a fight.
     * @param damage damage taken
     */
    public final void reduceHP(final int damage) {
        hp -= damage;
    }

    /**
     *  Method is used to award the hp from the angel.
     * @param newHP hp received from angel
     */
    public final void addHP(final int newHP) {
        hp += newHP;
    }

    /**
     *  Method is used to restore HP to 100% after level up.
     *  Method is abstract because every champion has different initial HP values
     *  and different HP per level growth.
     */
    public void restoreHP() {
        hp = hpStart + hpGrowth * level;
    }

    public abstract void applyStrategy();

    public abstract void isAttackedBy(Champion champion);

    public abstract void attack(Knight knight);

    public abstract void attack(Pyromancer pyromancer);

    public abstract void attack(Rogue rogue);

    public abstract void attack(Wizard wizard);

    public abstract void effectAppliedBy(Angel angel);
}
