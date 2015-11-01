package networkmonitor.parse;

import java.util.List;

import junit.framework.TestCase;
import networkmonitor.bolts.networkdata.NetworkDataBolt;

import networkmonitor.bolts.networkdata.PacketData;
import org.junit.Assert;
import org.junit.Test;

import util.json.JSONObject;
import util.json.JSONTokener;


public class ParseJsonPacketTest {

	@Test
	public void parseJson() {
		String json = "{\"PacketData\":[{\"TTL\":\"59\",\"data\":\"uOhWPQk4bHCf1cIeCABFAAA0riYAADsGr22611xUCgABBQG7wzPV3Ij4VsUdTIAQAXnYhwAAAQEICqIw8VU4EBcg\",\"destinationIP\":\"10.0.1.5\",\"destinationPort\":\"49971\",\"dsize\":\"52\",\"flagoffset\":\"0\",\"id\":\"44582\",\"sourceIP\":\"186.215.92.84\",\"sourcePort\":\"443\",\"tos\":\"0\"},{\"TTL\":\"64\",\"data\":\"bHCf1cIeuOhWPQk4CABFAAIpdX9AAEAGoR8KAAEFutdcVMMzAbtWxRtX1dyI+IAYEAAZTQAAAQEICjgQFyCiLxjRFwMDAfCTtCAG0ShmgOm9yTLq7ZNbt660KiMGBUhB3EXFQ3najtRAvTmAwajyzYD1xhHAm9cJeCUgh/qsSQmi/x6FsQWrEpX0PgUvAm1qDZEjUKOsFDzmj5qU86JOXRg2jXjHPofyBxvjZwdTzmp/HooyKOCdVxZmfA7J3EAD7Z687ZbdDXST1lXakafoYVJJocNB6S7tzGzf/uWFcmymiZh6zhnLbvor3EC1RetDhI53i9p9ytXGHRkAiA97QhXUXqzeuL25A4DwclCaDLMe/7zb+jyimsia+5yB4jMcP68n3SLf4BOY+XiaRw0gvy+T+hb5j5OUHmdKaYV8BgH0Mjkjp2SmCbGVX+sJBIeQQgyEwc7DPeQn/WZVRASKKLmGJKFqj735xYtL6FfXlB8F2sJPBF4mAQKcc2B54ginoPOqcgFuDDOte1UtwRfCcPYEgHy7dSMPHL25CHF+tkjN80YGpI3k8fk1stweCSCcRWMU/talh7wB6dIow4TefY+nxl95OCY7sBHnQ1H+ut0roZr/ZXzwk0Zk56R3awO59wtdw3XGWnGGEZaRGxDsJG82xZHGlYs+ZAmsu2cIvmybOFzQJihcOuYgLflPsx78cnFlsOzYU/14qtKoH7316hBWFJiOCG5GZTQnE1iS2VzPIAN7ca/T\",\"destinationIP\":\"186.215.92.84\",\"destinationPort\":\"443\",\"dsize\":\"553\",\"flagoffset\":\"0\",\"id\":\"30079\",\"sourceIP\":\"10.0.1.5\",\"sourcePort\":\"49971\",\"tos\":\"0\"},{\"TLL\":\"64\",\"data\":\"bHCf1cIeuOhWPQk4CABFAAA0To1AAEAGygYKAAEFutdcVMMzAbtWxR1M1dyJ94AQD/jH5QAAAQEICjgQF7eiMPHi\",\"destinationIP\":\"186.215.92.84\",\"destinationPort\":\"443\",\"dsize\":\"52\",\"flagoffset\":\"0\",\"id\":\"20109\",\"sourceIP\":\"10.0.1.5\",\"sourcePort\":\"49971\",\"tos\":\"0\"},{\"TLL\":\"59\",\"data\":\"uOhWPQk4bHCf1cIeCABFAAEzPDIAADsGIGO611xUCgABBQG7wzPV3Ij4VsUdTIAYAXmY2AAAAQEICqIw8eI4EBcgFwMDAFAysak2sM3IsD5wtHaitHTfGOdU1spmkDLgAIveBV46SGfZat/UbvatDdtzflZFp/9AO2+AxgbjCkt3Qmmbrk++cLwBcI5XKS9vm0L+m4wLVhcDAwBgzCZvWcHdOPTjs47FvGkgfhJrYzcubUTyfAQnR+9sjiFN2w3U203Wd/o4d7QC+GqKboShcQgGBeLh1iw7a/EPNGEP5E29SYTfdZRpTEpYv/Qnf8NOpr6LiWltQcnm98+QFwMDAEBX3it9FHQGXr3cjgtd9tZo919M7O7eNlRG0JJxJCwrA3X8ceIMBpj124T4m1FmrE1dfL1nTBogZMPdXGx0GhUL\",\"destinationIP\":\"10.0.1.5\",\"destinationPort\":\"49971\",\"dsize\":\"307\",\"flagoffset\":\"0\",\"id\":\"15410\",\"sourceIP\":\"186.215.92.84\",\"sourcePort\":\"443\",\"tos\":\"0\"},{\"TLL\":\"64\",\"data\":\"bHCf1cIeuOhWPQk4CABFAAB5u+dAAEAGXGcKAAEFutdcVMMzAbtWxR1M1dyJ94AYEADpcQAAAQEICjgQF7eiMPHiFwMDAEAziO9oLJs+JgFcCm+ZOobmk56dpgXOfoLofzbPncgWciNfk9y4m/DoWVfcKiZFH9L4qEDDF7vam1sGCAj/L25q\",\"destinationIP\":\"186.215.92.84\",\"destinationPort\":\"443\",\"dsize\":\"121\",\"flagoffset\":\"0\",\"id\":\"48103\",\"sourceIP\":\"10.0.1.5\",\"sourcePort\":\"49971\",\"tos\":\"0\"},{\"TLL\":\"59\",\"data\":\"uOhWPQk4bHCf1cIeCABFAAA0PDkAADsGIVu611xUCgABBQG7wzPV3In3VsUdkYAQAXnWFwAAAQEICqIw8eo4EBe3\",\"destinationIP\":\"10.0.1.5\",\"destinationPort\":\"49971\",\"dsize\":\"52\",\"flagoffset\":\"0\",\"id\":\"15417\",\"sourceIP\":\"186.215.92.84\",\"sourcePort\":\"443\",\"tos\":\"0\"}],\"hostname\":\"Joaos-MacBook-Pro.local\",\"topic\":\"NetworkData\"}";
		NetworkDataBolt bolt = new NetworkDataBolt("NetworkData");
		JSONObject jsonObj = new JSONObject(new JSONTokener(json));
		List<PacketData> packets;
		packets = bolt.parsePacket(jsonObj);
		
		Assert.assertEquals(59, packets.get(0).TTL);
		Assert.assertEquals("uOhWPQk4bHCf1cIeCABFAAA0riYAADsGr22611xUCgABBQG7wzPV3Ij4VsUdTIAQAXnYhwAAAQEICqIw8VU4EBcg", packets.get(0).data);

	}

}
