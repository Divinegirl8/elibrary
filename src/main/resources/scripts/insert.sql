-- Drop the foreign key constraint
ALTER TABLE liberian_reading_list DROP FOREIGN KEY FKcr61lqvny7x1wmf7chb6xdjak;


TRUNCATE TABLE liberian;

-- Recreate the foreign key constraint
ALTER TABLE liberian_reading_list
    ADD CONSTRAINT FKcr61lqvny7x1wmf7chb6xdjak
        FOREIGN KEY (liberian_id)
            REFERENCES liberian (id);


insert into liberian(id,username) values ('100','Derick');
