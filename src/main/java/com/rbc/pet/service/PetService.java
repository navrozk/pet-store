package com.rbc.pet.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Service;

import com.rbc.pet.domain.Category;
import com.rbc.pet.domain.Pet;
import com.rbc.pet.domain.Tag;

@Service
public class PetService {

	private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Autowired
	public PetService(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	@SuppressWarnings("unchecked")
	public Pet create(Pet pet) {
		
		// Insert into PET table and get the auto-generated petId
		String sql = "INSERT INTO PET (ID, CATEGORY_ID, NAME, PHOTO_URLS, STATUS) VALUES (DEFAULT, :CATEGORY_ID, :NAME, :PHOTO_URLS, :STATUS)";
		MapSqlParameterSource namedParameters = new MapSqlParameterSource("CATEGORY_ID", pet.getCategory().getId());
		namedParameters.addValue("NAME", pet.getName());
		namedParameters.addValue("STATUS", pet.getStatus());
      	String photoUrlsStr = null;
		List<String> photoUrls = pet.getPhotoUrls();
      	if (!photoUrls.isEmpty()) {
      		photoUrlsStr = StringUtils.join(photoUrls, ",");
      	}
      	namedParameters.addValue("PHOTO_URLS", photoUrlsStr);
      	GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
      	namedParameterJdbcTemplate.update(sql, namedParameters, generatedKeyHolder);
      	pet.setId((Integer)generatedKeyHolder.getKey());
      	
      	// Insert into PET_TAG table using auto-generated petId and tagId
      	sql = "INSERT INTO PET_TAG (PET_ID, TAG_ID) VALUES (:PET_ID, :TAG_ID)";
      	List<Tag> tags = pet.getTags();
      	List<Map<String, Object>> namedParametersList = new ArrayList<Map<String, Object>>();
      	if (tags != null) {
      		for (Tag tag : pet.getTags()) {
      			Map<String, Object> namedParametersMap = new HashMap<String, Object>();
      			namedParametersMap.put("PET_ID", pet.getId());
      			namedParametersMap.put("TAG_ID", tag.getId());
      	      	namedParametersList.add(namedParametersMap);
      		}      		
      	}
      	if (!namedParametersList.isEmpty()) {
      		namedParameterJdbcTemplate.batchUpdate(sql, namedParametersList.toArray(new HashMap[0]));
      	}
      	
		return pet;
	}

	public Pet read(Integer petId) {
		String sql = "SELECT P.*, C.NAME AS CATEGORY_NAME FROM PET P, CATEGORY C WHERE P.CATEGORY_ID = C.ID AND P.ID = :ID";
		SqlParameterSource namedParameters = new MapSqlParameterSource("ID", petId);
		Pet pet = (Pet) namedParameterJdbcTemplate.queryForObject(sql, namedParameters, new PetMapper());

		List<Tag> tags = new ArrayList<Tag>();
		
		sql = "SELECT PT.*, T.NAME AS TAG_NAME FROM PET_TAG PT, TAG T WHERE PT.TAG_ID = T.ID AND PT.PET_ID = :PET_ID";
		namedParameters = new MapSqlParameterSource("PET_ID", petId);
		@SuppressWarnings("unused")
		List<ResultSet> rsList = namedParameterJdbcTemplate.query(sql, namedParameters, new RowMapper<ResultSet>() {
			public ResultSet mapRow(ResultSet rs, int rowNum) throws SQLException {
				Tag tag = new Tag();
				tag.setId(rs.getInt("TAG_ID"));
				tag.setName(rs.getString("TAG_NAME"));
				tags.add(tag);
				return rs;
			}
		});

		if (tags.size() > 0) {
			pet.setTags(tags);
		}

		return pet;
	}

	public void delete(Integer petId) {
		String sql = "DELETE FROM PET WHERE ID = :ID";
		SqlParameterSource namedParameters = new MapSqlParameterSource("ID", petId);
		namedParameterJdbcTemplate.update(sql, namedParameters);
	}

	public List<Pet> readAll() {
		
		String sql = "SELECT P.*, C.NAME AS CATEGORY_NAME FROM PET P, CATEGORY C WHERE P.CATEGORY_ID = C.ID";
		List<Pet> pets = namedParameterJdbcTemplate.query(sql, new PetMapper());

		sql = "SELECT PT.*, T.NAME AS TAG_NAME FROM PET_TAG PT, TAG T WHERE PT.TAG_ID = T.ID";
		List<ResultSet> rsList = namedParameterJdbcTemplate.query(sql, new RowMapper<ResultSet>() {
			public ResultSet mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs;
			}
		});

		for (Pet p : pets) {
			List<Tag> tags = new ArrayList<Tag>();
			for (ResultSet rs : rsList) {
				try {
					if (p.getId() == rs.getInt("PET_ID")) {
						Tag tag = new Tag();
						tag.setId(rs.getInt("TAG_ID"));
						tag.setName(rs.getString("TAG_NAME"));
						tags.add(tag);
					}
				} catch (Exception e) {

				}
			}
			if (tags.size() > 0) {
				p.setTags(tags);
			}
		}

		return pets;
	}

	private static final class PetMapper implements RowMapper<Pet> {

		public Pet mapRow(ResultSet rs, int rowNum) throws SQLException {
			Pet pet = new Pet();

			pet.setId(rs.getInt("ID"));
			pet.setName(rs.getString("NAME"));
			pet.setStatus(rs.getString("STATUS"));

			Category category = new Category();
			category.setId(rs.getInt("CATEGORY_ID"));
			category.setName(rs.getString("CATEGORY_NAME"));
			pet.setCategory(category);

			String photoUrlsStr = rs.getString("PHOTO_URLS");
			List<String> photoUrls = null;
			if (photoUrlsStr != null) {
				photoUrls = Arrays.asList(photoUrlsStr.split("\\s*,\\s*"));
			}
			pet.setPhotoUrls(photoUrls);

			return pet;
		}
	}
}
