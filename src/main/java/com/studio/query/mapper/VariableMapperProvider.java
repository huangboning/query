package com.studio.query.mapper;

import com.studio.query.entity.Variable;
import com.studio.query.util.StringUtil;

public class VariableMapperProvider {
	public static String findVariable(Variable variable) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("select * from t_variable a where 1=1 ");
		if (variable.getVariableId() != 0) {
			buffer.append("and a.variable_id = #{variableId} ");
		}
		if (variable.getSceneId() != 0) {
			buffer.append("and a.scene_id = #{sceneId} ");
		}
		if (!StringUtil.isNullOrEmpty(variable.getVariableUUID())) {
			buffer.append("and a.variable_uuid =#{variableUUID} ");
		}
		if (!StringUtil.isNullOrEmpty(variable.getVariableName())) {
			buffer.append("and a.variable_name =#{variableName} ");
		}
		return buffer.toString();
	}
}
