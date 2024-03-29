package champion;

import angel.Angel;
import strategy.StrategyPyromancer;
import strategy.FirstStrategyPyromancer;
import strategy.SecondStrategyPyromancer;
import util.Constants;

public class Pyromancer extends Champion {

    Pyromancer(final int newPosX, final int newPosY) {
        setPosX(newPosX);
        setPosY(newPosY);
        setPreferredTerrain(Constants.PYRO_PREFERRED_TERRAIN);
        setTerrainModifier(Constants.PYRO_TERRAIN_MODIFIER);
        setHP(Constants.PYROMANCER_HP);
        setHPStart(Constants.PYROMANCER_HP);
        setHPGrowth(Constants.PYROMANCER_HP_GROWTH);
        setXP();
        setLevel();
        setIncapacitated(0);
        setFoughtThisRound(false);
        setFirstAbilityBase(Constants.PYRO_ABILITY_1_BASE);
        setFirstAbilityGrowth(Constants.PYRO_ABILITY_1_GROWTH);
        setSecondAbilityBase(Constants.PYRO_ABILITY_2_BASE);
        setSecondAbilityGrowth(Constants.PYRO_ABILITY_2_GROWTH);

        setRaceModKnightFirst(Constants.MODIFIER_20_POS);
        setRaceModKnightSecond(Constants.MODIFIER_20_POS);

        setRaceModPyromancerFirst(Constants.MODIFIER_10_NEG);
        setRaceModPyromancerSecond(Constants.MODIFIER_10_NEG);

        setRaceModRogueFirst(Constants.MODIFIER_20_NEG);
        setRaceModRogueSecond(Constants.MODIFIER_20_NEG);

        setRaceModWizardFirst(Constants.MODIFIER_5_POS);
        setRaceModWizardSecond(Constants.MODIFIER_5_POS);
    }

    /**
     *  Method used to apply the right strategy.
     */
    @Override
    public void applyStrategy() {
        StrategyPyromancer strategy;

        int hpLow = calculateTheoreticalHP() / Constants.PYROMANCER_HP_LOW;
        int hpHigh = calculateTheoreticalHP() / Constants.PYROMANCER_HP_HIGH;
        if (hpLow < getHP() && getHP() < hpHigh) {
            strategy = new FirstStrategyPyromancer();
            strategy.doStrategy(this);
        } else if (getHP() < hpLow) {
            strategy = new SecondStrategyPyromancer();
            strategy.doStrategy(this);
        }
    }

    /**
     *  Method used to change the race modifier.
     * @param newRaceMod race modifier to be added.
     */
    public void increaseRaceMod(final float newRaceMod) {
        setRaceModWizardFirst(getRaceModWizardFirst() + newRaceMod);
        setRaceModWizardSecond(getRaceModWizardSecond() + newRaceMod);
        setRaceModRogueFirst(getRaceModRogueFirst() + newRaceMod);
        setRaceModRogueSecond(getRaceModRogueSecond() + newRaceMod);
        setRaceModPyromancerFirst(getRaceModPyromancerFirst() + newRaceMod);
        setRaceModPyromancerSecond(getRaceModPyromancerSecond() + newRaceMod);
        setRaceModKnightFirst(getRaceModKnightFirst() + newRaceMod);
        setRaceModKnightSecond(getRaceModKnightSecond() + newRaceMod);
    }

    /**
     *  Method used to change the race modifier.
     * @param newRaceMod race modifier to be substracted
     */
    public void reduceRaceMod(final float newRaceMod) {
        setRaceModWizardFirst(getRaceModWizardFirst() - newRaceMod);
        setRaceModWizardSecond(getRaceModWizardSecond() - newRaceMod);
        setRaceModRogueFirst(getRaceModRogueFirst() - newRaceMod);
        setRaceModRogueSecond(getRaceModRogueSecond() - newRaceMod);
        setRaceModPyromancerFirst(getRaceModPyromancerFirst() - newRaceMod);
        setRaceModPyromancerSecond(getRaceModPyromancerSecond() - newRaceMod);
        setRaceModKnightFirst(getRaceModKnightFirst() - newRaceMod);
        setRaceModKnightSecond(getRaceModKnightSecond() - newRaceMod);
    }

    /**
     * Method is used to complete the double-dispatch mechanism.
     * @param champion that attacks "this"
     */
    @Override
    public void isAttackedBy(final Champion champion) {
        champion.attack(this);
    }

    /**
     *  Method is used to calculate the damage against a knight.
     * @param knight opponent champion.
     */
    @Override
    public void attack(final Knight knight) {
        // base damages
        float firstDamage = firstAbility();
        float secondDamage = secondAbility();
        float overTime = Constants.PYRO_OVERTIME_BASE + Constants.PYRO_OVERTIME_GROWTH * getLevel();

        // terrain modifier
        if (getApplyTerrainModifier()) {
            firstDamage += firstDamage * getTerrainModifier();
            secondDamage += secondDamage * getTerrainModifier();
            overTime += overTime * getTerrainModifier();
            firstDamage = Math.round(firstDamage);
            secondDamage = Math.round(secondDamage);
            overTime = Math.round((overTime));
        }

        // race modifier
        firstDamage *= getRaceModKnightFirst();
        secondDamage *= getRaceModKnightSecond();
        firstDamage = Math.round(firstDamage);
        secondDamage = Math.round(secondDamage);

        // DOT effects
        overTime *= getRaceModKnightSecond();
        int overTimeDamage = Math.round(overTime);
        if (knight.getDamageOverTime().size() > 0) {
            knight.resetDamageOverTime();
        }
        knight.addDamageOverTime(overTimeDamage, Constants.PYRO_OVERTIME_ROUNDS);
        // apply damage to enemy
        int totalDamage = Math.round(firstDamage) + Math.round(secondDamage);
        knight.reduceHP(totalDamage);
    }

    /**
     *  Method is used to calculate the damage against a pyromancer.
     * @param pyromancer opponent champion.
     */
    @Override
    public void attack(final Pyromancer pyromancer) {
        // base damages
        float firstDamage = firstAbility();
        float secondDamage = secondAbility();
        float overTime = Constants.PYRO_OVERTIME_BASE + Constants.PYRO_OVERTIME_GROWTH * getLevel();

        // terrain modifier
        if (getApplyTerrainModifier()) {
            firstDamage += firstDamage * getTerrainModifier();
            secondDamage += secondDamage * getTerrainModifier();
            overTime += overTime * getTerrainModifier();
            firstDamage = Math.round(firstDamage);
            secondDamage = Math.round(secondDamage);
            overTime = Math.round(overTime);
        }

        // race modifier
        firstDamage *= getRaceModPyromancerFirst();
        secondDamage *= getRaceModPyromancerSecond();
        firstDamage = Math.round(firstDamage);
        secondDamage = Math.round(secondDamage);

        // DOT effects
        overTime *= getRaceModPyromancerSecond();
        int overTimeDamage = Math.round(overTime);
        if (pyromancer.getDamageOverTime().size() > 0) {
            pyromancer.resetDamageOverTime();
        }
        pyromancer.addDamageOverTime(overTimeDamage, Constants.PYRO_OVERTIME_ROUNDS);
        // apply damage to enemy
        int totalDamage = Math.round(firstDamage) + Math.round(secondDamage);
        pyromancer.reduceHP(totalDamage);
    }

    /**
     *  Method is used to calculate the damage against a rogue.
     * @param rogue opponent champion.
     */
    @Override
    public void attack(final Rogue rogue) {
        // base damages
        float firstDamage = firstAbility();
        float secondDamage = secondAbility();
        float overTime = Constants.PYRO_OVERTIME_BASE + Constants.PYRO_OVERTIME_GROWTH * getLevel();

        // terrain modifier
        if (getApplyTerrainModifier()) {
            firstDamage += firstDamage * getTerrainModifier();
            secondDamage += secondDamage * getTerrainModifier();
            overTime += overTime * getTerrainModifier();
            firstDamage = Math.round(firstDamage);
            secondDamage = Math.round(secondDamage);
            overTime = Math.round(overTime);
        }

        // race modifier
        firstDamage *= getRaceModRogueFirst();
        secondDamage *= getRaceModRogueSecond();
        firstDamage = Math.round(firstDamage);
        secondDamage = Math.round(secondDamage);

        // DOT effects
        overTime *= getRaceModRogueSecond();
        int overTimeDamage = Math.round(overTime);
        if (rogue.getDamageOverTime().size() > 0) {
            rogue.resetDamageOverTime();
        }
        rogue.addDamageOverTime(overTimeDamage, Constants.PYRO_OVERTIME_ROUNDS);
        // apply damage to enemy
        int totalDamage = Math.round(firstDamage) + Math.round(secondDamage);
        rogue.reduceHP(totalDamage);
    }

    /**
     *  Method is used to calculate the damage against a wizard.
     * @param wizard opponent champion.
     */
    @Override
    public void attack(final Wizard wizard) {
        // base damages
        float firstDamage = firstAbility();
        float secondDamage = secondAbility();
        float overTime = Constants.PYRO_OVERTIME_BASE + Constants.PYRO_OVERTIME_GROWTH * getLevel();

        // terrain modifier
        if (getApplyTerrainModifier()) {
            firstDamage += firstDamage * getTerrainModifier();
            secondDamage += secondDamage * getTerrainModifier();
            overTime += overTime * getTerrainModifier();
            firstDamage = Math.round(firstDamage);
            secondDamage = Math.round(secondDamage);
            overTime = Math.round(overTime);
        }

        // race modifier
        firstDamage *= getRaceModWizardFirst();
        secondDamage *= (getRaceModWizardSecond() - Constants.FLOAT_PRECISION);
        firstDamage = Math.round(firstDamage);
        secondDamage = Math.round(secondDamage);

        // DOT effects
        overTime *= getRaceModWizardSecond();
        int overTimeDamage = Math.round(overTime);
        if (wizard.getDamageOverTime().size() > 0) {
            wizard.resetDamageOverTime();
        }
        wizard.addDamageOverTime(overTimeDamage, Constants.PYRO_OVERTIME_ROUNDS);
        // apply damage to enemy
        int totalDamage = Math.round(firstDamage) + Math.round(secondDamage);
        wizard.reduceHP(totalDamage);
    }

    /**
     *  Method used to apply the effect of the angel located on the tile.
     * @param angel angel that appeared on the same tile as the champion
     */
    @Override
    public void effectAppliedBy(final Angel angel) {
        angel.applyEffect(this);
    }
}
