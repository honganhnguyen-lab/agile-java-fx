package com.groupesan.project.java.scrumsimulator.mainpackage.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class BlockerStore {
    private List<Blocker1> list_of_blockers;
    private final File fL;
    private final ObjectMapper oMp = new ObjectMapper();
    private int lId = 0;
    private SolutionStore solutionStore;

    public BlockerStore() {
        String filePath = "src/main/resources/blocker.json";
        fL = new File(filePath);
        this.list_of_blockers = loading_Blockers();
        this.solutionStore = new SolutionStore();
        initializing_LstId();
    }

    private void initializing_LstId() {
        for (Blocker1 be : list_of_blockers) {
            int BId = Integer.parseInt(be.getBId().substring(1));
            if (BId > lId) {
                lId = BId;
            }
        }
    }

    private List<Blocker1> loading_Blockers() {
        try {
            if (fL.exists()) {
                System.out.println("File found: " + fL.getAbsolutePath());
                Map<String, List<Blocker1>> data = oMp.readValue(fL, new TypeReference<Map<String, List<Blocker1>>>() {});
                System.out.println("Parsed data: " + data);
                List<Blocker1> loadedBlockers = data.get("Blockers");
                if (loadedBlockers != null) {
                    System.out.println("Loaded blockers: " + loadedBlockers);
                    return loadedBlockers;
                } else {
                    System.out.println("No blockers found in file.");
                    return new ArrayList<>();
                }
            } else {
                System.out.println("File does not exist: " + fL.getAbsolutePath());
            }
        } catch (IOException e) {
            System.err.println("Failure of loading blockers from file: " + fL.getAbsolutePath());
            System.err.println("Error: " + e.getMessage());
        }
        return new ArrayList<>();
    }

    private void saving_Blockers() {
        try {
            Map<String, List<Blocker1>> data = new HashMap<>();
            data.put("Blockers", list_of_blockers);
            oMp.writeValue(fL, data);
            System.out.println("Blockers saved to file: " + fL.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Failed to save blockers to file: " + fL.getAbsolutePath());
            System.err.println("Error: " + e.getMessage());
        }
    }

    public String gId() {
        lId++;
        return "B" + String.format("%02d", lId);
    }

    public List<Blocker1> getBlK() {
        return list_of_blockers;
    }

    public Blocker1 getBlKById(String BId) {
        for (Blocker1 el : list_of_blockers) {
            if (el.getBId().equals(BId)) {
                return el;
            }
        }
        return null;
    }

    public void addBlK(Blocker1 blocker) {
        list_of_blockers.add(blocker);
        saving_Blockers();
    }

    public void updateBlk(Blocker1 updatedBlocker) {
        for (int k = 0; k < list_of_blockers.size(); k++) {
            if (list_of_blockers.get(k).getBId().equals(updatedBlocker.getBId())) {
                list_of_blockers.set(k, updatedBlocker);
                break;
            }
        }
        saving_Blockers();
    }

    public boolean deleteBlk(String BId) {
        boolean isBlockerDeleted = removeBlockerFromStore(BId);
        list_of_blockers.removeIf(blocker -> blocker.getBId().equals(BId));
        saving_Blockers();
        if (isBlockerDeleted) {
            solutionStore.deleteSolutionsByBlockerId(BId);
            System.out.println("Deleted blocker and associated solutions" + BId);
            return true;
        } else {
            System.out.println("Failed to delete blocker" + BId);
            return false;
        }

    }

    // code reference -> https://stackoverflow.com/questions/13213395/adjusting-xorshift-generator-to-return-a-number-within-a-maximum

    public class XorShift {
        private long seed;

        public XorShift(long seed) {
            this.seed = seed;
        }

        public int nextInt(int bound) {
            seed ^= (seed << 21);
            seed ^= (seed >>> 35);
            seed ^= (seed << 4);
            return (int) (Math.abs(seed) % bound);
        }
    }


    public void randomizeProbabilities() {
        XorShift xorShift = new XorShift(System.nanoTime());

        for (Blocker1 blocker : list_of_blockers) {
            int randomProbability = xorShift.nextInt(101);
            blocker.setProbability(randomProbability);
        }
        saving_Blockers();
    }




    private boolean removeBlockerFromStore(String blockerId) {
        return true;
    }
}
