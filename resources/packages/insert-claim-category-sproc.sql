
CREATE PROCEDURE Prototype.insertClaimCategory(
IN idparam integer, 
IN code varchar(10), 
IN description varchar(30), 
IN created_user varchar(30), 
IN updated_user varchar(30))
BEGIN 
	IF NOT EXISTS (select ID from CLAIM_CATEGORIES_LKUP where ID = idparam) THEN
		insert into CLAIM_CATEGORIES_LKUP (ID, CODE, DESCRIPTION, CREATED_USER, UPDATED_USER) 
		values (idparam, code, description, created_user, updated_user);
	END IF;
END //