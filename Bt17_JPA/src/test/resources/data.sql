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


