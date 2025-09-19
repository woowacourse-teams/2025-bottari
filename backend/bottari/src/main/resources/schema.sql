CREATE FULLTEXT INDEX idx_template_title
    ON bottari_template(title)
    WITH PARSER ngram;

CREATE INDEX idx_created_at_id
    ON bottari_template(created_at desc, id desc);

CREATE INDEX idx_taken_count_id
    ON bottari_template(taken_count desc, id desc);
