package com.fasttasker.fast_tasker.domain.task;

/**
 * 
 */
public enum TaskStatus {
    /**
     * the task is public and bids are being accepted.
     * no one has been hired yet.
     */
    ACTIVE,

    /**
     * the owner accepted an offer. The money is being held in escrow.
     * the Tasker is committed to completing the job on the agreed-upon date.
     * no new offers are being accepted.
     */
    ASSIGNED,

    /**
     * physical work has begun. Useful if you need to track time or location in real time.
     * if you don't need that much precision, you can skip this and go from ASSIGNED to COMPLETED.
     */
    IN_PROGRESS,

    /**
     * the job is complete and the payment has been released to the Tasker.
     * cycle successfully completed.
     */
    COMPLETED,

    /**
     * the task was canceled by the owner or there was a problem and it was canceled afterward
     */
    CANCELLED
}