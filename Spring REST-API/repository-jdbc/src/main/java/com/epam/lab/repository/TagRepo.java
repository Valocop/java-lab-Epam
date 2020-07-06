package com.epam.lab.repository;

import com.epam.lab.model.Tag;

public interface TagRepo extends Repository<Tag, Long> {
    boolean deleteUnsignedTags();
}
