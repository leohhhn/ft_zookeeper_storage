// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: account_service.proto

package com.leon.gRPC;

public interface LogResponseOrBuilder extends
    // @@protoc_insertion_point(interface_extends:LogResponse)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>.LogStatus status = 1;</code>
   * @return The enum numeric value on the wire for status.
   */
  int getStatusValue();
  /**
   * <code>.LogStatus status = 1;</code>
   * @return The status.
   */
  com.leon.gRPC.LogStatus getStatus();

  /**
   * <code>uint64 entryAtIndex = 2;</code>
   * @return The entryAtIndex.
   */
  long getEntryAtIndex();

  /**
   * <code>uint64 lastEntryIndex = 3;</code>
   * @return Whether the lastEntryIndex field is set.
   */
  boolean hasLastEntryIndex();
  /**
   * <code>uint64 lastEntryIndex = 3;</code>
   * @return The lastEntryIndex.
   */
  long getLastEntryIndex();
}