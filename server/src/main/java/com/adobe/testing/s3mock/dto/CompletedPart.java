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

package com.adobe.testing.s3mock.dto;

import static com.adobe.testing.s3mock.util.EtagUtil.normalizeEtag;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * <a href="https://docs.aws.amazon.com/AmazonS3/latest/API/API_CompletedPart.html">API Reference</a>.
 */
@JsonRootName("CompletedPart")
public class CompletedPart {

  @JsonProperty("PartNumber")
  protected Integer partNumber;

  @JsonProperty("ETag")
  protected String etag;

  public CompletedPart(@JsonProperty("PartNumber") Integer partNumber,
      @JsonProperty("ETag") String etag) {
    this.partNumber = partNumber;
    this.etag = normalizeEtag(etag);
  }

  public Integer getPartNumber() {
    return partNumber;
  }

  public String getETag() {
    return etag;
  }
}
