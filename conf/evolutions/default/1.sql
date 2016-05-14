# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table administrateur (
  id                        integer auto_increment not null,
  statut                    varchar(255),
  son_utilisateur_id        bigint,
  referent_cfa              tinyint(1) default 0,
  constraint uq_administrateur_son_utilisateur_id unique (son_utilisateur_id),
  constraint pk_administrateur primary key (id))
;

create table batiment (
  id                        integer auto_increment not null,
  libelle                   varchar(255),
  son_universite_id         integer,
  constraint pk_batiment primary key (id))
;

create table cours (
  id                        integer auto_increment not null,
  type                      varchar(255),
  type_detaille             varchar(255),
  heure_debut               datetime,
  heure_fin                 datetime,
  son_enseignant_id         integer,
  sa_matiere_id             integer,
  sa_salle_id               bigint,
  sa_periode_id             integer,
  sa_promo_id               bigint,
  signature_enseignant      tinyint(1) default 0,
  constraint pk_cours primary key (id))
;

create table enseignant (
  id                        integer auto_increment not null,
  statut                    varchar(255),
  son_utilisateur_id        bigint,
  constraint uq_enseignant_son_utilisateur_id unique (son_utilisateur_id),
  constraint pk_enseignant primary key (id))
;

create table etudiant (
  id                        bigint auto_increment not null,
  uid                       varchar(255),
  numero_etudiant           varchar(255),
  statut                    varchar(255),
  son_utilisateur_id        bigint,
  constraint uq_etudiant_son_utilisateur_id unique (son_utilisateur_id),
  constraint pk_etudiant primary key (id))
;

create table filiere (
  id                        integer auto_increment not null,
  codefiliere               varchar(255),
  libelle                   varchar(255),
  annee                     varchar(255),
  son_batiment_id           integer,
  constraint pk_filiere primary key (id))
;

create table matiere (
  id                        integer auto_increment not null,
  libelle                   varchar(255),
  libelle_abregee           varchar(255),
  semestre                  varchar(255),
  nombre_heures             bigint,
  constraint pk_matiere primary key (id))
;

create table module (
  id                        bigint auto_increment not null,
  libelle                   varchar(255),
  constraint pk_module primary key (id))
;

create table periode (
  id                        integer auto_increment not null,
  libelle                   varchar(255),
  date_de_deut              datetime,
  date_fin                  datetime,
  constraint pk_periode primary key (id))
;

create table presence (
  id                        integer auto_increment not null,
  emergement                tinyint(1) default 0,
  motif                     varchar(255),
  justificatif              varchar(255),
  son_cours_id              integer,
  son_etudiant_id           bigint,
  constraint pk_presence primary key (id))
;

create table promotion (
  id                        bigint auto_increment not null,
  annee_scolaire            varchar(255),
  groupe                    varchar(255),
  type                      varchar(255),
  filiere                   varchar(255),
  constraint pk_promotion primary key (id))
;

create table salle (
  id                        bigint auto_increment not null,
  libelle                   varchar(255),
  son_batiment_id           integer,
  constraint pk_salle primary key (id))
;

create table universite (
  id                        integer auto_increment not null,
  libelle                   varchar(255),
  constraint pk_universite primary key (id))
;

create table utilisateur (
  id                        bigint auto_increment not null,
  nom                       varchar(255),
  prenom                    varchar(255),
  adresse_mail              varchar(255),
  mot_de_passe              varchar(255),
  date_de_naissance         datetime,
  lien_photo                varchar(255),
  constraint pk_utilisateur primary key (id))
;


create table enseignant_matiere (
  enseignant_id                  integer not null,
  matiere_id                     integer not null,
  constraint pk_enseignant_matiere primary key (enseignant_id, matiere_id))
;

create table promotion_etudiant (
  promotion_id                   bigint not null,
  etudiant_id                    bigint not null,
  constraint pk_promotion_etudiant primary key (promotion_id, etudiant_id))
;

create table promotion_matiere (
  promotion_id                   bigint not null,
  matiere_id                     integer not null,
  constraint pk_promotion_matiere primary key (promotion_id, matiere_id))
;

create table utilisateur_module (
  utilisateur_id                 bigint not null,
  module_id                      bigint not null,
  constraint pk_utilisateur_module primary key (utilisateur_id, module_id))
;
alter table administrateur add constraint fk_administrateur_sonUtilisateur_1 foreign key (son_utilisateur_id) references utilisateur (id) on delete restrict on update restrict;
create index ix_administrateur_sonUtilisateur_1 on administrateur (son_utilisateur_id);
alter table batiment add constraint fk_batiment_sonUniversite_2 foreign key (son_universite_id) references universite (id) on delete restrict on update restrict;
create index ix_batiment_sonUniversite_2 on batiment (son_universite_id);
alter table cours add constraint fk_cours_sonEnseignant_3 foreign key (son_enseignant_id) references enseignant (id) on delete restrict on update restrict;
create index ix_cours_sonEnseignant_3 on cours (son_enseignant_id);
alter table cours add constraint fk_cours_saMatiere_4 foreign key (sa_matiere_id) references matiere (id) on delete restrict on update restrict;
create index ix_cours_saMatiere_4 on cours (sa_matiere_id);
alter table cours add constraint fk_cours_saSalle_5 foreign key (sa_salle_id) references salle (id) on delete restrict on update restrict;
create index ix_cours_saSalle_5 on cours (sa_salle_id);
alter table cours add constraint fk_cours_saPeriode_6 foreign key (sa_periode_id) references periode (id) on delete restrict on update restrict;
create index ix_cours_saPeriode_6 on cours (sa_periode_id);
alter table cours add constraint fk_cours_saPromo_7 foreign key (sa_promo_id) references promotion (id) on delete restrict on update restrict;
create index ix_cours_saPromo_7 on cours (sa_promo_id);
alter table enseignant add constraint fk_enseignant_sonUtilisateur_8 foreign key (son_utilisateur_id) references utilisateur (id) on delete restrict on update restrict;
create index ix_enseignant_sonUtilisateur_8 on enseignant (son_utilisateur_id);
alter table etudiant add constraint fk_etudiant_sonUtilisateur_9 foreign key (son_utilisateur_id) references utilisateur (id) on delete restrict on update restrict;
create index ix_etudiant_sonUtilisateur_9 on etudiant (son_utilisateur_id);
alter table filiere add constraint fk_filiere_sonBatiment_10 foreign key (son_batiment_id) references batiment (id) on delete restrict on update restrict;
create index ix_filiere_sonBatiment_10 on filiere (son_batiment_id);
alter table presence add constraint fk_presence_sonCours_11 foreign key (son_cours_id) references cours (id) on delete restrict on update restrict;
create index ix_presence_sonCours_11 on presence (son_cours_id);
alter table presence add constraint fk_presence_sonEtudiant_12 foreign key (son_etudiant_id) references etudiant (id) on delete restrict on update restrict;
create index ix_presence_sonEtudiant_12 on presence (son_etudiant_id);
alter table salle add constraint fk_salle_sonBatiment_13 foreign key (son_batiment_id) references batiment (id) on delete restrict on update restrict;
create index ix_salle_sonBatiment_13 on salle (son_batiment_id);



alter table enseignant_matiere add constraint fk_enseignant_matiere_enseignant_01 foreign key (enseignant_id) references enseignant (id) on delete restrict on update restrict;

alter table enseignant_matiere add constraint fk_enseignant_matiere_matiere_02 foreign key (matiere_id) references matiere (id) on delete restrict on update restrict;

alter table promotion_etudiant add constraint fk_promotion_etudiant_promotion_01 foreign key (promotion_id) references promotion (id) on delete restrict on update restrict;

alter table promotion_etudiant add constraint fk_promotion_etudiant_etudiant_02 foreign key (etudiant_id) references etudiant (id) on delete restrict on update restrict;

alter table promotion_matiere add constraint fk_promotion_matiere_promotion_01 foreign key (promotion_id) references promotion (id) on delete restrict on update restrict;

alter table promotion_matiere add constraint fk_promotion_matiere_matiere_02 foreign key (matiere_id) references matiere (id) on delete restrict on update restrict;

alter table utilisateur_module add constraint fk_utilisateur_module_utilisateur_01 foreign key (utilisateur_id) references utilisateur (id) on delete restrict on update restrict;

alter table utilisateur_module add constraint fk_utilisateur_module_module_02 foreign key (module_id) references module (id) on delete restrict on update restrict;

# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table administrateur;

drop table batiment;

drop table cours;

drop table enseignant;

drop table enseignant_matiere;

drop table etudiant;

drop table filiere;

drop table matiere;

drop table module;

drop table periode;

drop table presence;

drop table promotion;

drop table promotion_etudiant;

drop table promotion_matiere;

drop table salle;

drop table universite;

drop table utilisateur;

drop table utilisateur_module;

SET FOREIGN_KEY_CHECKS=1;

