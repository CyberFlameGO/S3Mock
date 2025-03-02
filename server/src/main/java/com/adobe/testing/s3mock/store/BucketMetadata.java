/*
 *  Copyright 2017-2022 Adobe.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.adobe.testing.s3mock.store;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.adobe.testing.s3mock.dto.BucketLifecycleConfiguration;
import com.adobe.testing.s3mock.dto.ObjectLockConfiguration;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents a bucket in S3, used to serialize and deserialize all metadata locally.
 */
public class BucketMetadata {

  private String name;

  private String creationDate;

  private ObjectLockConfiguration objectLockConfiguration;
  private BucketLifecycleConfiguration bucketLifecycleConfiguration;
  private Path path;

  private Map<String, UUID> objects = new HashMap<>();

  public BucketLifecycleConfiguration getBucketLifecycleConfiguration() {
    return bucketLifecycleConfiguration;
  }

  public void setBucketLifecycleConfiguration(
      BucketLifecycleConfiguration bucketLifecycleConfiguration) {
    this.bucketLifecycleConfiguration = bucketLifecycleConfiguration;
  }

  public ObjectLockConfiguration getObjectLockConfiguration() {
    return objectLockConfiguration;
  }

  public void setObjectLockConfiguration(
      ObjectLockConfiguration objectLockConfiguration) {
    this.objectLockConfiguration = objectLockConfiguration;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(String creationDate) {
    this.creationDate = creationDate;
  }

  public Path getPath() {
    return path;
  }

  public void setPath(Path path) {
    this.path = path;
  }

  public Map<String, UUID> getObjects() {
    return objects;
  }

  public void setObjects(Map<String, UUID> objects) {
    this.objects = objects;
  }

  public boolean doesKeyExist(String key) {
    return getID(key) != null;
  }

  public UUID addKey(String key) {
    if (doesKeyExist(key)) {
      return getID(key);
    } else {
      UUID uuid = UUID.nameUUIDFromBytes(key.getBytes(UTF_8));
      this.objects.put(key, uuid);
      return uuid;
    }
  }

  public boolean removeKey(String key) {
    UUID removed = this.objects.remove(key);
    return removed != null;
  }

  public UUID getID(String key) {
    return this.objects.get(key);
  }

  @Override
  public String toString() {
    return "BucketMetadata{"
        + "name='" + name + '\''
        + ", creationDate='" + creationDate + '\''
        + ", path=" + path
        + ", objects=" + objects
        + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BucketMetadata that = (BucketMetadata) o;
    return Objects.equals(name, that.name) && Objects.equals(creationDate,
        that.creationDate) && Objects.equals(objectLockConfiguration,
        that.objectLockConfiguration) && Objects.equals(path, that.path)
        && Objects.equals(objects, that.objects);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, creationDate, objectLockConfiguration, path, objects);
  }
}
