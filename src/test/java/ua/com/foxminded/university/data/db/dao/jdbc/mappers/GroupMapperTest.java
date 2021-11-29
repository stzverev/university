package ua.com.foxminded.university.data.db.dao.jdbc.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import ua.com.foxminded.university.data.ConfigTest;
import ua.com.foxminded.university.data.model.Group;

@SpringJUnitConfig(ConfigTest.class)
@ExtendWith(MockitoExtension.class)
class GroupMapperTest {

    private GroupMapper groupMapper;

    @Autowired
    private void setGroupMapper(GroupMapper groupMapper) {
        this.groupMapper = groupMapper;
    }

    @Test
    void shouldGetGroup() throws SQLException {
        Group group = new Group();
        group.setName("B1");
        group.setId(1);
        Group expected = group;

        ResultSet resultSet = getGroupResultSetMock(group);
        Group actual = groupMapper.mapRow(resultSet, 1);

        assertEquals(expected, actual);
    }

    private ResultSet getGroupResultSetMock(Group group) throws SQLException {
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getLong("group_id")).thenReturn(group.getId());
        when(resultSet.getString("group_name")).thenReturn(group.getName());
        return resultSet;
    }

}
