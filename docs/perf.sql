select count(*) from watch;

select count(*) ,avg(time), sum(time), sum(time) / total, (select sum(time) from watch) total, name from watch group by name;


select count(*) n, avg(time), max(time), sum(time), name from watch group by name order by name;

select min(num) || '-' || max(num), avg(num), count(num), name from num group by name order by name;