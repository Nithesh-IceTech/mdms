select entrytime as entry_time, tsdb_query.* from (
    select %s,
           public.time_bucket_gapfill(%s, entry_time, %s, %s) as entrytime,
           public.interpolate(sum(total_kwhp)) as total_kwhp,
           sum(total_kwhp) as total_kwhp_orig,
           public.interpolate(sum(total_kwhn)) as total_kwhn,
           public.interpolate(sum(total_kvarp)) as total_kvarp,
           public.interpolate(sum(total_kvarn)) as total_kvarn,
           public.interpolate(sum(t1_kwhp)) as t1_kwhp,
           sum(t1_kwhp) as t1_kwhp_orig,
           public.interpolate(sum(t1_kwhn)) as t1_kwhn,
           public.interpolate(sum(t1_kvarp)) as t1_kvarp,
           public.interpolate(sum(t1_kvarn)) as t1_kvarn,
           public.interpolate(sum(t2_kwhp)) as t2_kwhp,
           sum(t2_kwhp) as t2_kwhp_orig,
           public.interpolate(sum(t2_kwhn)) as t2_kwhn,
           public.interpolate(sum(t2_kvarp)) as t2_kvarp,
           public.interpolate(sum(t2_kvarn)) as t2_kvarn,
           public.interpolate(avg(rms_l1_v)) as rms_l1_v,
           avg(rms_l1_v) as rms_l1_v_orig,
           public.interpolate(avg(rms_l2_v)) as rms_l2_v,
           public.interpolate(avg(rms_l3_v)) as rms_l3_v,
           public.interpolate(avg(rms_l1_c)) as rms_l1_c,
           avg(rms_l1_c) as rms_l1_c_orig,
           public.interpolate(avg(rms_l2_c)) as rms_l2_c,
           public.interpolate(avg(rms_l3_c)) as rms_l3_c,
           public.interpolate(sum(volume_1)) as volume_1,
           sum(volume_1) as volume_1_orig
    from meter_reading
    where %s
      and entry_time >= %s and entry_time < %s
    group by %s, entrytime
    order by entrytime asc

) tsdb_query;