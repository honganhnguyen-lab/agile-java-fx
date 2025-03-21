package com.groupesan.project.java.scrumsimulator.mainpackage.impl;

import java.util.List;
import java.util.Map;

public class SolutionManager {

    private SolutionStore solnStr;

    public SolutionManager() {
        this.solnStr = new SolutionStore();
    }

    public Map<String, String> getAllBlockers() {
        return solnStr.getBlkrs();
    }

    public List<Solution> getSolutionsForBlocker(String blkId) {
        return solnStr.getSolnsByBlkrId(blkId);
    }

    public boolean addSolution(String sname, String blkId, int probability) {
        Solution newSoln = SolutionFactory.createSolution(sname, blkId, probability);
        boolean isAddedFlg = solnStr.addSolution(newSoln);
        if (isAddedFlg) {
            System.out.println("solution id added: " + newSoln);
        } else {
            System.out.println("Failed! Soln could not be added: " + newSoln);
        }
        return isAddedFlg;
    }

    public boolean updateSolution(String sname, String blkId, String newSname, int probability) {
        Solution exSoln = solnStr.findSolutionByNameAndBlockerId(sname, blkId);
        if (exSoln != null) {
            exSoln.setSname(newSname);
            exSoln.setProbability(probability);
            boolean isUpdated = solnStr.updateSolution(exSoln);
            if (isUpdated) {
                System.out.println("Successfully updated solution: " + exSoln);
            } else {
                System.out.println("Failed! could not update solution: " + exSoln);
            }
            return isUpdated;
        }
        System.out.println("Solution could not be found for update: sname=" + sname + ", blockerId=" + blkId);
        return false;
    }

    public boolean deleteSolution(String sname, String blkId) {
        boolean isDeletedFlag = solnStr.deleteSolutionByNameAndBlockerId(sname, blkId);
        if (isDeletedFlag) {
            System.out.println("Deleted solution with sname: " + sname + " and blockerId: " + blkId);
        } else {
            System.out.println("Failed! could not delete solution with sname: " + sname + " and blockerId: " + blkId);
        }
        return isDeletedFlag;
    }

    public boolean randomizeProbabilitiesForSolutions(String blkId) {
        return solnStr.randomizeProbabilities(blkId);
    }


}