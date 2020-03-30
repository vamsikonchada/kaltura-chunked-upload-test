
import com.kaltura.client.enums.*;
import com.kaltura.client.types.*;
import com.kaltura.client.utils.response.base.Response;
import com.kaltura.client.services.*;
import com.kaltura.client.services.MediaService.AddContentMediaBuilder;
import com.kaltura.client.services.MediaService.GetMediaBuilder;
import com.kaltura.client.services.MediaService.UpdateContentMediaBuilder;
import com.kaltura.client.APIOkRequestsExecutor;
import com.kaltura.client.Client;
import com.kaltura.client.Configuration;

import chunkedupload.ParallelUpload;

public class UploadTest { 
	public static void main(String[] argv){
		try{
			if (argv.length < 4){
				System.out.println("Usage: <service URL> <partner ID> <partner admin secret> </path/to/file> [optional entryId to update]\n");
				System.exit (1);
			}
			try{
				Configuration config = new Configuration();
				config.setEndpoint(argv[0]);
				//config.setProxy("www-proxy-hqdc.us.oracle.com");
				//config.setProxyPort(80);

				Client client = new Client(config);

				String secret = argv[2];
				int partnerId = Integer.parseInt(argv[1]);
				String ks = client.generateSessionV2(secret, null, SessionType.ADMIN, partnerId, 86400, "");

				client.setSessionId(ks);
				System.out.println(ks);

				MediaEntry newEntry = null;
				boolean update = false;
				if(argv.length > 4 && argv[4] != "")
				{
					GetMediaBuilder getBuilder = MediaService.get(argv[4]);
					Response<MediaEntry> response = (Response<MediaEntry>)APIOkRequestsExecutor.getExecutor().execute(getBuilder.build(client));
					if (response != null)
					{
						if (response.error != null)
						{
							throw response.error;
						}
						newEntry = response.results;
						if (newEntry != null)
						{
							update = true;
						}
					}
				} else {
					MediaEntry entry = new MediaEntry();
					entry.setName("Chunked Upload Test");
					entry.setType(EntryType.MEDIA_CLIP);
					entry.setMediaType(MediaType.VIDEO);
					newEntry = entry;
				}

				System.out.println("\nCreated a new entry: " + newEntry.getId());
				
				ParallelUpload pu = new ParallelUpload(client, argv[3]);	
				String tokenId = pu.upload();
				if (tokenId != null) {
					UploadedFileTokenResource fileTokenResource = new UploadedFileTokenResource();
					fileTokenResource.setToken(tokenId);
					if(update == true)
					{
						UpdateContentMediaBuilder requestBuilder = MediaService.updateContent(newEntry.getId(), fileTokenResource);
						Response<MediaEntry> response = (Response<MediaEntry>)APIOkRequestsExecutor.getExecutor().execute(requestBuilder.build(client));
						if (response != null)
						{
							if (response.error != null)
							{
								throw response.error;
							}

							newEntry = response.results;
						}
					} else {
						AddContentMediaBuilder requestBuilder = MediaService.addContent(newEntry.getId(), fileTokenResource);
						Response<MediaEntry> response = (Response<MediaEntry>)APIOkRequestsExecutor.getExecutor().execute(requestBuilder.build(client));
						if (response != null)
						{
							if (response.error != null)
							{
								throw response.error;
							}

							newEntry = response.results;
						}
					}

					System.out.println("\nUploaded a new Video file to entry: " + newEntry.getId());
				}
			}
			catch (APIException e)
			{
				e.printStackTrace();
			}
		} catch (Exception exc) {
        	exc.printStackTrace();
    	}
	}
}
