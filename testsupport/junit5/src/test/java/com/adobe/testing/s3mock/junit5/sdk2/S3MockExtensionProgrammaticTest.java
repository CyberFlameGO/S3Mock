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

package com.adobe.testing.s3mock.junit5.sdk2;

import static org.assertj.core.api.Assertions.assertThat;

import com.adobe.testing.s3mock.junit5.S3MockExtension;
import com.adobe.testing.s3mock.util.DigestUtil;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

/**
 * Tests and demonstrates the usage of the {@link S3MockExtension}
 * for the SDK v2.
 * */
class S3MockExtensionProgrammaticTest {

  @RegisterExtension
  static final S3MockExtension S3_MOCK =
      S3MockExtension.builder().silent().withSecureConnection(false).build();

  private static final String BUCKET_NAME = "my-demo-test-bucket";
  private static final String UPLOAD_FILE_NAME = "src/test/resources/sampleFile.txt";

  private final S3Client s3Client = S3_MOCK.createS3ClientV2();

  /**
   * Creates a bucket, stores a file, downloads the file again and compares checksums.
   *
   * @throws Exception if FileStreams can not be read
   */
  @Test
  void shouldUploadAndDownloadObject() throws Exception {
    final File uploadFile = new File(UPLOAD_FILE_NAME);

    s3Client.createBucket(CreateBucketRequest.builder().bucket(BUCKET_NAME).build());
    s3Client.putObject(
        PutObjectRequest.builder().bucket(BUCKET_NAME).key(uploadFile.getName()).build(),
        RequestBody.fromFile(uploadFile));

    final ResponseInputStream<GetObjectResponse> response =
        s3Client.getObject(
            GetObjectRequest.builder().bucket(BUCKET_NAME).key(uploadFile.getName()).build());

    final InputStream uploadFileIs = Files.newInputStream(uploadFile.toPath());
    final String uploadDigest = DigestUtil.hexDigest(uploadFileIs);
    final String downloadedDigest = DigestUtil.hexDigest(response);
    uploadFileIs.close();
    response.close();

    assertThat(uploadDigest).isEqualTo(downloadedDigest).as(
        "Up- and downloaded Files should have equal digests");
  }
}
