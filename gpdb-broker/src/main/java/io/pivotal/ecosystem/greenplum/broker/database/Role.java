/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.pivotal.ecosystem.greenplum.broker.database;

import org.springframework.stereotype.Component;

import io.pivotal.ecosystem.greenplum.broker.util.Utils;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.SQLException;

@Component
public class Role {

    public void createRoleForInstance(String instanceId) throws SQLException {
        Utils.checkValidUUID(instanceId);
        GreenplumDatabase.executeUpdate("CREATE ROLE \"" + instanceId + "\" LOGIN");
        GreenplumDatabase.executeUpdate("ALTER DATABASE \"" + instanceId + "\" OWNER TO \"" + instanceId + "\"");
    }

    public void deleteRole(String instanceId) throws SQLException {
        Utils.checkValidUUID(instanceId);
        GreenplumDatabase.executeUpdate("DROP ROLE IF EXISTS \"" + instanceId + "\"");
    }

    public String bindRoleToDatabase(String dbInstanceId) throws SQLException {
        Utils.checkValidUUID(dbInstanceId);

        SecureRandom random = new SecureRandom();
        String passwd = new BigInteger(130, random).toString(32);

        GreenplumDatabase.executeUpdate("ALTER ROLE \"" + dbInstanceId + "\" LOGIN password '" + passwd + "'");
        return passwd;
    }

    public void unBindRoleFromDatabase(String dbInstanceId) throws SQLException{
        Utils.checkValidUUID(dbInstanceId);
        GreenplumDatabase.executeUpdate("ALTER ROLE \"" + dbInstanceId + "\" NOLOGIN");
    }
}
