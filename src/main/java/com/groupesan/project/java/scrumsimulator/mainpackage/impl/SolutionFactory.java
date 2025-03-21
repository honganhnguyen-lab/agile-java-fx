package com.groupesan.project.java.scrumsimulator.mainpackage.impl;

import java.util.List;

public class SolutionFactory {

    public static Solution createSolution(String sname, String blkrId, int probability) {
        String nxtId = gnrtNxtSlnId();
        return new Solution(nxtId, sname, blkrId, probability);
    }


    private static String gnrtNxtSlnId() {
        SolutionStore solnStore = new SolutionStore();
        List<Solution> exSolns = solnStore.getAllSolutions();
        int maxId = 0;

        for (Solution soln : exSolns) {
            String sid = soln.getSid();
            if (sid.startsWith("solution")) {
                try {
                    int currIdN = Integer.parseInt(sid.substring(8));
                    if (currIdN > maxId) {
                        maxId = currIdN;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Wrong format in solution ID: " + sid + ". " + e.getMessage());
                }
            }
        }

        return "solution" + (maxId + 1);
    }
}