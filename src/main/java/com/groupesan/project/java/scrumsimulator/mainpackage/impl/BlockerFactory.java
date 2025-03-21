package com.groupesan.project.java.scrumsimulator.mainpackage.impl;

public class BlockerFactory {
    private final BlockerStore blockerSt;

    public BlockerFactory(BlockerStore blockerSt) {
        this.blockerSt = blockerSt;
    }

    public Blocker1 createBlocker(String Bname, String desc, int probability) {
        String nBId = blockerSt.gId();
        Blocker1 blocker = new Blocker1(nBId, Bname, desc, probability);
        blockerSt.addBlK(blocker);
        return blocker;
    }
}
