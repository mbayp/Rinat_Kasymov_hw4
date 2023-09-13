import java.util.Random;
import java.util.Scanner;

public class Main {

    public static int bossHealth = 700;
    public static int bossDamage = 50;
    public static String bossDefence;
    public static int[] heroesHealth = {280, 270, 250, 300}; // Добавили Golem и его здоровье
    public static int[] heroesDamage = {15, 20, 25, 0}; // Добавили Golem и его урон
    public static int medicHealth = 300; // Здоровье медика
    public static int witcherHealth = 150; // Здоровье Witcher
    public static boolean witcherUsedRevive = false; // Флаг, чтобы Witcher мог использовать способность оживления только 1 раз
    public static boolean bossStunned = false; // Флаг, чтобы отслеживать, оглушен ли босс
    public static String[] heroesAttackType = {"Physical", "Magical", "Kinetic", "Medic", "Golem", "Lucky", "Witcher", "Thor"};
    public static int roundNumber;

    public static void main(String[] args) {
        showStatistics();
        while (!isGameOver()) {
            playRound();
        }
    }

    public static void playRound() {
        roundNumber++;
        chooseBossDefence();
        bossHits();
        if (!bossStunned) {
            heroesHit();
        } else {
            System.out.println("Boss is stunned and skips this round!");
            bossStunned = false; // Оглушение действует только на один раунд
        }
        showStatistics();
        medicHeal();
        witcherRevive();
    }

    public static void chooseBossDefence() {
        Random random = new Random();
        int randomIndex = random.nextInt(heroesAttackType.length); // 0, 1, 2, ..., 7
        bossDefence = heroesAttackType[randomIndex];
    }

    public static void bossHits() {
        for (int i = 0; i < heroesHealth.length; i++) {
            if (heroesHealth[i] > 0) {
                int damage = bossDamage;
                if (heroesAttackType[i] == "Golem") {
                    damage -= bossDamage / 5; // Golem принимает 1/5 урона от босса
                }
                if (heroesHealth[i] - damage < 0) {
                    heroesHealth[i] = 0;
                } else {
                    heroesHealth[i] = heroesHealth[i] - damage;
                }
            }
        }
    }

    public static void heroesHit() {
        for (int i = 0; i < heroesDamage.length; i++) {
            if (heroesHealth[i] > 0 && bossHealth > 0) {
                int damage = heroesDamage[i];
                if (heroesAttackType[i].equals(bossDefence)) {
                    Random random = new Random();
                    int coeff = random.nextInt(9) + 2; // 2, 3, 4, 5, 6, 7, 8, 9, 10
                    damage = heroesDamage[i] * coeff;
                    System.out.println("Critical damage: " + damage);
                }
                if (bossHealth - damage < 0) {
                    bossHealth = 0;
                } else {
                    bossHealth = bossHealth - damage;
                }
                if (heroesAttackType[i].equals("Lucky")) {
                    Scanner random = null;
                    if (random.nextBoolean()) {
                        System.out.println("Lucky dodged the boss's attack!");
                        bossHealth += bossDamage; // Lucky уклонился, босс нанес урон сам себе
                    }
                }
            }
        }
    }

    public static void medicHeal() {
        if (medicHealth > 0) {
            for (int i = 0; i < heroesHealth.length; i++) {
                if (heroesHealth[i] > 0 && heroesHealth[i] < 100) {
                    int medicHill = 150;
                    heroesHealth[i] += medicHill;
                    System.out.println("Medic healed " + heroesAttackType[i] + " for " + medicHill + " health.");
                    break; // Медик может лечить только одного героя за раунд
                }
            }
            medicHealth -= bossDamage; // Медик получает урон от босса
        }
    }

    public static void witcherRevive() {
        if (witcherHealth > 0 && !witcherUsedRevive) {
            for (int i = 0; i < heroesHealth.length; i++) {
                if (heroesHealth[i] == 0) {
                    heroesHealth[i] = witcherHealth;
                    witcherUsedRevive = true;
                    System.out.println("Witcher revived " + heroesAttackType[i] + " with " + witcherHealth + " health.");
                    witcherHealth = 0; // Witcher жертвует своим здоровьем, чтобы оживить героя
                    break; // Witcher может использовать способность оживления только один раз
                }
            }
        }
    }

    public static boolean isGameOver() {
        if (bossHealth <= 0) {
            System.out.println("Heroes won!!!");
            return true;
        }
        boolean allHeroesDead = true;
        for (int i = 0; i < heroesHealth.length; i++) {
            if (heroesHealth[i] > 0) {
                allHeroesDead = false;
                break;
            }
        }
        if (bossHealth <= 0) {
            System.out.println("Heroes won!!!");
            return true;
        }
        if (heroesHealth[0] <= 0 && heroesHealth[1] <= 0 && heroesHealth[2] <= 0) {
            System.out.println("Boss won!!!");
            return true;
        }
        return allHeroesDead;
    }

    public static void showStatistics() {
        System.out.println("ROUND " + roundNumber + " -------------");
        System.out.println("Boss health: " + bossHealth + " damage: " + bossDamage + " defence: " +
                (bossDefence == null ? "No defence" : bossDefence));
        for (int i = 0; i < heroesHealth.length; i++) {
            System.out.println(heroesAttackType[i] + " health: " + heroesHealth[i] + " damage: " + heroesDamage[i]);
        }
        System.out.println("Witcher health: " + witcherHealth);
    }
}