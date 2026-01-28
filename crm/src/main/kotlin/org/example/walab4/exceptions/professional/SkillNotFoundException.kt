package org.example.walab4.exceptions.professional

class SkillNotFoundException(skillId: String? = null, cause: Throwable? = null): RuntimeException("Skill $skillId not found", cause )