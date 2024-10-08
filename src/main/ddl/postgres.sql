CREATE TABLE MYTHIC_RECORD
(
    RECORD_ID VARCHAR(100) NOT NULL,
    SEASON NUMERIC(5) NOT NULL,
    PERIOD NUMERIC(10) NOT NULL,
    DUNGEON_ID NUMERIC(10) NOT NULL,
    DURATION NUMERIC(10) NOT NULL,
    COMPLETED_TIMESTAMP NUMERIC(20) NOT NULL,
    KEYSTONE_LEVEL NUMERIC(5) NOT NULL,
    KEYSTONE_UPGRADE NUMERIC(5) NOT NULL,
    MYTHIC_RATING NUMERIC(10,5) NULL,
    JSON_TEXT TEXT NOT NULL,
    CONSTRAINT MYTHIC_RECORD_PK PRIMARY KEY (RECORD_ID)
)
;
CREATE INDEX MYTHIC_RECORD_INDEX_PERIOD ON MYTHIC_RECORD(PERIOD)
;
CREATE INDEX MYTHIC_RECORD_INDEX_PERIOD_LEVEL ON MYTHIC_RECORD(PERIOD, KEYSTONE_LEVEL)
;
CREATE TABLE MYTHIC_RECORD_PLAYER
(
    RECORD_ID VARCHAR(100) NOT NULL,
    PLAYER_REALM VARCHAR(50) NOT NULL,
    PLAYER_NAME VARCHAR(100) NOT NULL,
    SPEC_ID NUMERIC(5) NOT NULL,
    CLASS_NAME VARCHAR(20) NOT NULL,
    SPEC_NAME VARCHAR(20) NOT NULL,
    ROLE_NAME VARCHAR(20) NOT NULL,
    PLAYER_ID NUMERIC(20) NULL,
    ID SERIAL,
    CONSTRAINT MYTHIC_RECOERD_PLAYER_PK PRIMARY KEY (ID)
)
;
CREATE INDEX MYTHIC_RECORD_PLAYER_INDEX_REALM_NAME ON MYTHIC_RECORD_PLAYER(PLAYER_REALM, PLAYER_NAME);
;
CREATE INDEX MYTHIC_RECORD_PLAYER_INDEX_PLAYERID ON MYTHIC_RECORD_PLAYER(PLAYER_ID)
;
CREATE INDEX MYTHIC_RECORD_PLAYER_INDEX_RECORDID ON MYTHIC_RECORD_PLAYER(RECORD_ID)
;
CREATE TABLE MYTHIC_PLAYER
(
    PLAYER_REALM VARCHAR(50) NOT NULL,
    PLAYER_NAME VARCHAR(100) NOT NULL,
    SPEC_ID NUMERIC(5) NOT NULL,
    CLASS_NAME VARCHAR(20) NOT NULL,
    SPEC_NAME VARCHAR(20) NOT NULL,
    LAST_UPDATE_TS NUMERIC(20) NOT NULL,
    CONSTRAINT MYTHIC_PLAYER_PK PRIMARY KEY (PLAYER_REALM,PLAYER_NAME)
)
;
CREATE TABLE MYTHIC_BOTUSER
(
    ID VARCHAR(20) NOT NULL,
    WEB_SESSION_ID VARCHAR(100) NOT NULL,
    CONSTRAINT MYTHIC_BOTUSER_PK PRIMARY KEY (ID)
)
;
CREATE TABLE MYTHIC_BOTUSER_PLAYER
(
    ID SERIAL NOT NULL,
    BOTUSER_ID VARCHAR(20) NOT NULL,
    PLAYER_REALM VARCHAR(50) NOT NULL,
    PLAYER_NAME VARCHAR(100) NOT NULL,
    CONSTRAINT MYTHIC_BOTUSER_PLAYER_PK PRIMARY KEY (ID)
)
;
CREATE TABLE MYTHIC_BOTUSER_COMMENT
(
    BOTUSER_ID VARCHAR(20) NOT NULL,
    PLAYER_REALM VARCHAR(200) NOT NULL,
    PLAYER_NAME VARCHAR(100) NOT NULL,
    COMMENTS VARCHAR(1000) NOT NULL,
    CONSTRAINT MYTHIC_BOTUSER_COMMENT_PK PRIMARY KEY (BOTUSER_ID,PLAYER_REALM,PLAYER_NAME)
)
;
DROP TABLE MYTHIC_AUCTION;
CREATE TABLE MYTHIC_AUCTION
(
    AUCTION_ID NUMERIC(20) NOT NULL,
    REALM_ID NUMERIC(20) NOT NULL,
    ITEM_ID VARCHAR(200) NOT NULL,
    FIRST_SEEN_TS NUMERIC(20) NOT NULL,
    LAST_SEEN_TS NUMERIC(20) NOT NULL,
    JSON_TEXT TEXT NOT NULL,
    CONSTRAINT MYTHIC_AUCTION_PK PRIMARY KEY (AUCTION_ID)
)
;
DROP TABLE MYTHIC_ITEM;
CREATE TABLE MYTHIC_ITEM
(
    ITEM_ID VARCHAR(200) NOT NULL,
    JSON_TEXT TEXT NOT NULL,
    CONSTRAINT MYTHIC_ITEM_PK PRIMARY KEY (ITEM_ID)
)
;
CREATE TABLE MYTHIC_DUNGEON
(
    DUNGEON_ID NUMERIC(10) NOT NULL,
    DUNGEON_NAME VARCHAR(100) NOT NULL,
    ZONE VARCHAR(100) NULL,
    UPGRADE_1 NUMERIC(10) NULL,
    UPGRADE_2 NUMERIC(10) NULL,
    UPGRADE_3 NUMERIC(10) NULL,
    CONSTRAINT MYTHIC_DUNGEON_PK PRIMARY KEY (DUNGEON_ID)
)
;
CREATE TABLE PLAYER_TALENT
(
    PLAYER_REALM VARCHAR(50) NOT NULL,
    PLAYER_NAME VARCHAR(100) NOT NULL,
    SPEC_ID NUMERIC(5) NOT NULL,
    TALENT_CODE VARCHAR(300) NOT NULL,
    LAST_UPDATE_TS NUMERIC(20) NOT NULL,
    CONSTRAINT PLAYER_TALENT_PK PRIMARY KEY (PLAYER_REALM, PLAYER_NAME, SPEC_ID)
)
;
CREATE TABLE PLAYER_TALENT_SLOT
(
    TALENT_CODE VARCHAR(300) NOT NULL,
    TALENT_ID NUMERIC(10) NOT NULL,
    TALENT_RANK NUMERIC(5) NOT NULL,
    TALENT_NAME VARCHAR(100) NOT NULL,
    TOOLTIP_ID NUMERIC(10) NOT NULL,
    SPELL_ID NUMERIC(10) NOT NULL,
    CONSTRAINT PLAYER_TALENT_SLOT_PK PRIMARY KEY (TALENT_CODE, TALENT_ID)
)
;
CREATE TABLE PLAYER_REALM
(
    REALM_ID NUMERIC(10) NOT NULL,
    REALM_SLUG VARCHAR(50) NOT NULL,
    REALM_NAME VARCHAR(50) NOT NULL,
    CONSTRAINT PLAYER_REALM_PK PRIMARY KEY (REALM_ID)
)
;
CREATE TABLE MYTHIC_SEASON
(
    SEASON NUMERIC(10) NOT NULL,
    SEASON_NAME VARCHAR(100) NULL,
    START_TIMESTAMP NUMERIC(20) NOT NULL,
    END_TIMESTAMP NUMERIC(20) NULL,
    CONSTRAINT MYTHIC_SEASON_PK PRIMARY KEY (SEASON)
)
;
CREATE TABLE MYTHIC_SEASON_PERIOD
(
    SEASON NUMERIC(10) NOT NULL,
    PERIOD NUMERIC(10) NOT NULL,
    CONSTRAINT MYTHIC_SEASON_PERIOD_PK PRIMARY KEY (SEASON, PERIOD)
)
;
CREATE TABLE MYTHIC_SEASON_DUNGEON
(
    SEASON NUMERIC(10) NOT NULL,
    DUNGEON_ID NUMERIC(10) NOT NULL,
    CONSTRAINT MYTHIC_SEASON_DUNGEON_PK PRIMARY KEY (SEASON, DUNGEON_ID)
)
;
CREATE TABLE MYTHIC_PERIOD
(
    PERIOD NUMERIC(10) NOT NULL,
    START_TIMESTAMP NUMERIC(20) NOT NULL,
    END_TIMESTAMP NUMERIC(20) NOT NULL,
    CONSTRAINT MYTHIC_PERIOD_PK PRIMARY KEY (PERIOD)
)
;
