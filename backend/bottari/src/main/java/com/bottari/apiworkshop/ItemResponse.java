package com.bottari.apiworkshop;

record ItemResponse(
        int rank,
        String name,
        Long includedCount
) {

    public static ItemResponse of(int rank, ItemProjection projection) {
        return new ItemResponse(
                rank,
                projection.getName().name(),
                projection.getIncludedCount()
        );
    }
}
