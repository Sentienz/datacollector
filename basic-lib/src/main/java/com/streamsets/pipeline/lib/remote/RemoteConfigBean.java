/*
 * Copyright 2019 StreamSets Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.streamsets.pipeline.lib.remote;

import com.streamsets.pipeline.api.ConfigDef;
import com.streamsets.pipeline.api.ValueChooserModel;
import com.streamsets.pipeline.api.credential.CredentialValue;
import com.streamsets.pipeline.lib.tls.KeyStoreType;
import com.streamsets.pipeline.lib.tls.KeyStoreTypeChooserValues;

public class RemoteConfigBean {

  @ConfigDef(
      required = true,
      type = ConfigDef.Type.STRING,
      label = "Resource URL",
      description = "Specify the SFTP/FTP URL",
      displayPosition = 10,
      group = "#0"
  )
  public String remoteAddress;

  @ConfigDef(
      required = true,
      type = ConfigDef.Type.BOOLEAN,
      defaultValue = "true",
      label = "Path Relative to User Home Directory",
      description = "If checked, the path is resolved relative to the logged in user's home directory, " +
          "if a username is entered in the Credentials tab or in the URL.",
      displayPosition = 20,
      group = "#0"
  )
  public boolean userDirIsRoot = true;

  @ConfigDef(
      required = true,
      type = ConfigDef.Type.BOOLEAN,
      defaultValue = "false",
      label = "Create Path",
      description = "If checked, the path will be created if it does not exist",
      displayPosition = 30,
      group = "#0"
  )
  public boolean createPathIfNotExists;

  @ConfigDef(
      required = false,
      type = ConfigDef.Type.MODEL,
      defaultValue = "NONE",
      label = "Authentication",
      description = "The authentication method to use to login to remote server",
      displayPosition = 10,
      group = "#1"
  )
  @ValueChooserModel(AuthenticationChooserValues.class)
  public Authentication auth = Authentication.NONE;

  @ConfigDef(
      required = true,
      type = ConfigDef.Type.CREDENTIAL,
      label = "Username",
      description = "Username to use to login to the remote server",
      displayPosition = 15,
      group = "#1",
      dependsOn = "auth",
      triggeredByValue = {"PASSWORD", "PRIVATE_KEY"}
  )
  public CredentialValue username;

  @ConfigDef(
      required = true,
      type = ConfigDef.Type.CREDENTIAL,
      label = "Password",
      description = "Password to use to login to the remote server. If private key is specified, that is used.",
      displayPosition = 20,
      group = "#1",
      dependsOn = "auth",
      triggeredByValue = {"PASSWORD"}
  )
  public CredentialValue password;

  @ConfigDef(
      required = false,
      type = ConfigDef.Type.MODEL,
      defaultValue = "FILE",
      label = "Private Key Provider",
      description = "Provide the private key via a file or plain text",
      displayPosition = 25,
      group = "#1",
      dependsOn = "auth",
      triggeredByValue = {"PRIVATE_KEY"}
  )
  @ValueChooserModel(PrivateKeyProviderChooserValues.class)
  public PrivateKeyProvider privateKeyProvider = PrivateKeyProvider.FILE;

  @ConfigDef(
      required = true,
      type = ConfigDef.Type.STRING,
      label = "Private Key File",
      description = "Private key file to use to login to the remote server.",
      displayPosition = 30,
      group = "#1",
      dependsOn = "privateKeyProvider",
      triggeredByValue = {"FILE"}
  )
  public String privateKey;

  @ConfigDef(
      required = true,
      type = ConfigDef.Type.CREDENTIAL,
      label = "Private Key",
      description = "Private key to use to login to the remote server",
      displayPosition = 30,
      group = "#1",
      dependsOn = "privateKeyProvider",
      triggeredByValue = {"PLAIN_TEXT"}
  )
  public CredentialValue privateKeyPlainText;

  @ConfigDef(
      required = false,
      type = ConfigDef.Type.CREDENTIAL,
      label = "Private Key Passphrase",
      description = "Passphrase to use to open the private key file.",
      displayPosition = 40,
      group = "#1",
      dependsOn = "auth",
      triggeredByValue = {"PRIVATE_KEY"}
  )
  public CredentialValue privateKeyPassphrase;

  @ConfigDef(
      required = true,
      type = ConfigDef.Type.BOOLEAN,
      defaultValue = "true",
      label = "Strict Host Checking",
      description = "If enabled, will only connect to the host if the host is in the known hosts file.",
      displayPosition = 50,
      group = "#1"
  )
  public boolean strictHostChecking;

  @ConfigDef(
      required = true,
      type = ConfigDef.Type.STRING,
      label = "Known Hosts file",
      description = "Full path to the file that lists the host keys of all known hosts." +
          "This must be specified if the strict host checking is enabled.",
      group = "#1",
      displayPosition = 60,
      dependsOn = "strictHostChecking",
      triggeredByValue = "true"
  )
  public String knownHosts;


  @ConfigDef(
      required = true,
      type = ConfigDef.Type.BOOLEAN,
      defaultValue = "false",
      label = "Use Client Certificate for FTPS",
      description = "Enable this if the FTPS Server requires mutual authentication. The client will need to provide " +
          "a keystore file containing the client certificate.",
      displayPosition = 70,
      group = "#1"
  )
  public boolean useFTPSClientCert;

  @ConfigDef(
      required = true,
      type = ConfigDef.Type.STRING,
      label = "FTPS Client Certificate Keystore File",
      description = "Full path to the keystore file containing the client certificate",
      displayPosition = 71,
      group = "#1",
      dependsOn = "useFTPSClientCert",
      triggeredByValue = "true"
  )
  public String ftpsClientCertKeystoreFile;

  @ConfigDef(
      required = true,
      type = ConfigDef.Type.MODEL,
      defaultValue = "JKS",
      label = "FTPS Client Certificate Keystore Type",
      description = "The FTPS Client Certificate Keystore type",
      displayPosition = 72,
      group = "#1",
      dependsOn = "useFTPSClientCert",
      triggeredByValue = "true"
  )
  @ValueChooserModel(KeyStoreTypeChooserValues.class)
  public KeyStoreType ftpsClientCertKeystoreType = KeyStoreType.JKS;

  @ConfigDef(
      required = false,
      type = ConfigDef.Type.CREDENTIAL,
      label = "FTPS Client Certificate Keystore Password",
      description = "The password to the FTPS Client Certificate Keystore File, if applicable.  " +
          "Using a password is highly recommended for security reasons.",
      displayPosition = 73,
      group = "#1",
      dependsOn = "useFTPSClientCert",
      triggeredByValue = "true"
  )
  public CredentialValue ftpsClientCertKeystorePassword = () -> "";

  @ConfigDef(
      required = true,
      type = ConfigDef.Type.STRING,
      label = "FTPS Truststore File",
      description = "Full path to the truststore file containing the server certificate",
      displayPosition = 81,
      group = "#1",
      dependsOn = "ftpsTrustStoreProvider",
      triggeredByValue = "FILE"
  )
  public String ftpsTruststoreFile;

  @ConfigDef(
      required = true,
      type = ConfigDef.Type.MODEL,
      defaultValue = "JKS",
      label = "FTPS Truststore Type",
      description = "The FTPS Truststore type",
      displayPosition = 82,
      group = "#1",
      dependsOn = "ftpsTrustStoreProvider",
      triggeredByValue = "FILE"
  )
  @ValueChooserModel(KeyStoreTypeChooserValues.class)
  public KeyStoreType ftpsTruststoreType = KeyStoreType.JKS;

  @ConfigDef(
      required = false,
      type = ConfigDef.Type.CREDENTIAL,
      label = "FTPS Truststore Password",
      description = "The password to the FTPS Truststore file, if applicable.  " +
          "Using a password is highly recommended for security reasons.",
      displayPosition = 83,
      group = "#1",
      dependsOn = "ftpsTrustStoreProvider",
      triggeredByValue = "FILE"
  )
  public CredentialValue ftpsTruststorePassword = () -> "";

  @ConfigDef(
      required = true,
      type = ConfigDef.Type.BOOLEAN,
      defaultValue = "false",
      label = "Disable Read Ahead Stream",
      description = "If checked, disable the read-ahead streaming functionality of the SSH client.  Disable if" +
          " experiencing problems with larger files (ex: in whole file).  Note that this will also result in" +
          " significantly reducing performance.",
      displayPosition = 100,
      group = "#0"
  )
  public boolean disableReadAheadStream;

  @ConfigDef(
      required = false,
      type = ConfigDef.Type.NUMBER,
      defaultValue = "0",
      label = "Socket Timeout",
      description = "The socket timeout in seconds. A value of 0 indicates no timeout.",
      displayPosition = 110,
      group = "#0",
      min = 0
  )
  public int socketTimeout = 0;

  @ConfigDef(
      required = false,
      type = ConfigDef.Type.NUMBER,
      defaultValue = "0",
      label = "Connection Timeout",
      description = "The connection timeout in seconds. A value of 0 indicates no timeout.",
      displayPosition = 111,
      group = "#0",
      min = 0
  )
  public int connectionTimeout = 0;

  @ConfigDef(
      required = false,
      type = ConfigDef.Type.NUMBER,
      defaultValue = "0",
      label = "Data Timeout",
      description = "The data timeout in seconds. A value of 0 indicates no timeout.",
      displayPosition = 112,
      group = "#0",
      min = 0
  )
  public int dataTimeout = 0;

}
