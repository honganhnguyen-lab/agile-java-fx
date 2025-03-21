package com.groupesan.project.java.scrumsimulator.mainpackage.impl;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SolutionStore {

    private static final String SOLN_PATH = "src/main/resources/solution.json";
    private static final String BLKR_PATH = "src/main/resources/blocker.json";

    public Map<String, String> getBlkrs() {
        Map<String, String> blkrMap = new HashMap<>();
        try (FileInputStream fis = new FileInputStream(new File(BLKR_PATH))) {
            JSONArray blkrsArr = new JSONObject(new JSONTokener(fis)).optJSONArray("Blockers");
            if (blkrsArr != null) {
                for (int i = 0; i < blkrsArr.length(); i++) {
                    JSONObject blkr = blkrsArr.getJSONObject(i);
                    blkrMap.put(blkr.getString("bid"), blkr.getString("bname"));
                }
            }
        } catch (IOException e) {
            System.out.println("Failed! could not load blockers from blocker.json file: " + e.getMessage());
        }
        return blkrMap;
    }

    public List<Solution> getSolnsByBlkrId(String blkId) {
        List<Solution> solns = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(new File(SOLN_PATH))) {
            JSONArray solnsArray = new JSONObject(new JSONTokener(fis)).optJSONArray("Solutions");
            if (solnsArray != null) {
                for (int i = 0; i < solnsArray.length(); i++) {
                    JSONObject solnObj = solnsArray.getJSONObject(i);
                    if (solnObj.getString("blockerId").equals(blkId)) {
                        solns.add(new Solution(
                                solnObj.getString("sid"),
                                solnObj.getString("sname"),
                                blkId,
                                solnObj.getInt("probability")));
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Failed! could not load solns for blockerId: " + blkId + ". " + e.getMessage());
        }
        return solns;
    }

    public boolean addSolution(Solution soln) {
        JSONArray solnsArray = loadSolutionsArray();
        JSONObject newSoln = new JSONObject();
        newSoln.put("sid", soln.getSid());
        newSoln.put("probability", soln.getProbability());
        newSoln.put("sname", soln.getSname());
        newSoln.put("blockerId", soln.getBlockerId());
        solnsArray.put(newSoln);
        return writeSolutionsArray(solnsArray);
    }

    public boolean updateSolution(Solution soln) {
        JSONArray solnsArray = loadSolutionsArray();
        for (int i = 0; i < solnsArray.length(); i++) {
            JSONObject solnObj = solnsArray.getJSONObject(i);
            if (solnObj.getString("sid").equals(soln.getSid())) {
                solnObj.put("sname", soln.getSname());
                solnObj.put("probability", soln.getProbability());
                return writeSolutionsArray(solnsArray);
            }
        }
        return false;
    }

    public boolean deleteSolutionByNameAndBlockerId(String sname, String blkrId) {
        JSONArray solnsArray = loadSolutionsArray();
        for (int i = 0; i < solnsArray.length(); i++) {
            JSONObject solnObj = solnsArray.getJSONObject(i);
            if (solnObj.getString("sname").equals(sname) && solnObj.getString("blockerId").equals(blkrId)) {
                solnsArray.remove(i);
                return writeSolutionsArray(solnsArray);
            }
        }
        return false;
    }

    public void deleteSolutionsByBlockerId(String bId) {
        JSONArray solnArray = loadSolutionsArray();
        JSONArray uptdArr = new JSONArray();

        for (int i = 0; i < solnArray.length(); i++) {
            JSONObject solnObj = solnArray.getJSONObject(i);
            if (!solnObj.getString("blockerId").equals(bId)) {
                uptdArr.put(solnObj);
            }
        }

        if (writeSolutionsArray(uptdArr)) {
            System.out.println("Deleted solutions associated with blockerId: " + bId);
        } else {
            System.out.println("Failed to update solution.json after deleting solutions for blockerId: " + bId);
        }
    }

    public Solution findSolutionByNameAndBlockerId(String sname, String blkrId) {
        JSONArray solnsArray = loadSolutionsArray();
        for (int i = 0; i < solnsArray.length(); i++) {
            JSONObject solnObj = solnsArray.getJSONObject(i);
            if (solnObj.getString("sname").equals(sname) && solnObj.getString("blockerId").equals(blkrId)) {
                return new Solution(
                        solnObj.getString("sid"),
                        solnObj.getString("sname"),
                        solnObj.getString("blockerId"),
                        solnObj.getInt("probability"));
            }
        }
        return null;
    }

    public List<Solution> getAllSolutions() {
        List<Solution> solns = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(new File(SOLN_PATH))) {
            JSONArray solnsArray = new JSONObject(new JSONTokener(fis)).optJSONArray("Solutions");
            if (solnsArray != null) {
                for (int i = 0; i < solnsArray.length(); i++) {
                    JSONObject solnObj = solnsArray.getJSONObject(i);
                    solns.add(new Solution(
                            solnObj.getString("sid"),
                            solnObj.getString("sname"),
                            solnObj.getString("blockerId"),
                            solnObj.getInt("probability")));
                }
            }
        } catch (IOException e) {
            System.out.println("Failed! could not load solns from solution.json file: " + e.getMessage());
        }
        return solns;
    }

    private JSONArray loadSolutionsArray() {
        try (FileInputStream fis = new FileInputStream(new File(SOLN_PATH))) {
            return new JSONObject(new JSONTokener(fis)).optJSONArray("Solutions");
        } catch (IOException e) {
            System.out.println("Failed! could not load solns from solution.json file " + e.getMessage());
            return new JSONArray();
        }
    }

    private boolean writeSolutionsArray(JSONArray solnsArray) {
        try (FileWriter writer = new FileWriter(SOLN_PATH, StandardCharsets.UTF_8)) {
            JSONObject root = new JSONObject();
            root.put("Solutions", solnsArray);
            writer.write(root.toString(4));
            return true;
        } catch (IOException e) {
            System.out.println("Failed! could not write solns from solution.json file " + e.getMessage());
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


    public boolean randomizeProbabilities(String blockerId) {
        JSONArray solnArr = loadSolutionsArray();
        XorShift xorShift = new XorShift(System.nanoTime());

        for (int i = 0; i < solnArr.length(); i++) {
            JSONObject soln = solnArr.getJSONObject(i);
            if (soln.getString("blockerId").equals(blockerId)) {
                int randomProbability = xorShift.nextInt(101);
                soln.put("probability", randomProbability);
            }
        }

        return writeSolutionsArray(solnArr);
    }
}
