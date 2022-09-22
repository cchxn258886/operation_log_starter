package com.hzwotu.operationlogsdk.dto;

/**
 * @Author chenl
 * @Date 2022/8/17 2:04 下午
 */

public class SubStringPosition {
    private int left;
    private int right;

    public SubStringPosition() {
    }

    public SubStringPosition(int left, int right) {
        this.left = left;
        this.right = right;
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getRight() {
        return right;
    }

    public void setRight(int right) {
        this.right = right;
    }
}
