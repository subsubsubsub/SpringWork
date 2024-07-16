-- @Test 가 동작할때 이 쿼리가 H2 db에 실행된다.

insert into t_user (`id`, `name`, `email`, `created_at`, `updated_at`)
values (default, 'martin', 'martin@redknight.com', now(), now());
insert into t_user (`id`, `name`, `email`, `created_at`, `updated_at`)
values (default, 'dennis', 'dennis@reddragon.com', now(), now());
insert into t_user (`id`, `name`, `email`, `created_at`, `updated_at`)
values (default, 'sophia', 'sophia@blueknight.com', now(), now());
insert into t_user (`id`, `name`, `email`, `created_at`, `updated_at`)
values (default, 'james', 'james@bluedragon.com', now(), now());
insert into t_user (`id`, `name`, `email`, `created_at`, `updated_at`)
values (default, 'martin', 'martin@another.com', now(), now());

-- book , publisher 추가
insert into publisher(`id`, `name`) values (default, '더테크출판');


-- insert into book(`id`, `name`, `publisher_id`, `created_at`, `updated_at`) values (default, 'JPA 완전정복', 1, now(), now());
-- insert into book(`id`, `name`, `publisher_id`, `created_at`, `updated_at`) values (default, '스프링 시큐리티', 1, now(), now());
-- insert into book(`id`, `name`, `publisher_id`, `created_at`, `updated_at`) values (default, '스프링 부트', 1, now(), now());

insert into book(`id`, `name`, `publisher_id`, `status`,`created_at`, `updated_at`) values (default, 'JPA 완전정복', 1, 100,now(), now());
insert into book(`id`, `name`, `publisher_id`, `status`,`created_at`, `updated_at`) values (default, '스프링 시큐리티', 1, 200,now(), now());
insert into book(`id`, `name`, `publisher_id`, `status`,`created_at`, `updated_at`) values (default, '스프링 부트', 1, 100,now(), now());

