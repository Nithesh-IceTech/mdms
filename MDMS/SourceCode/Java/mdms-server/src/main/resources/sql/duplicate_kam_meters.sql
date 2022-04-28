SELECT substr(ref,instr(ref,'/meters/')+length('/meters/'))
from KAMSTRUP_METER group by substr(ref,instr(ref,'/meters/')+length('/meters/')) HAVING count(*) > 1
