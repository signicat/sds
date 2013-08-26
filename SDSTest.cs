using Microsoft.VisualStudio.TestTools.UnitTesting;
using System.IO;
using System.Net;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Threading.Tasks;

namespace Signicat.Support.SDS.Test
{
    [TestClass]
    public class SDSTest
    {
        [TestMethod]
        public async Task Uploading_a_PDF_document_to_SDS()
        {
            var httpClientHandler = new HttpClientHandler { Credentials = new NetworkCredential("shared", "Bond007") };
            using (var client = new HttpClient(httpClientHandler))
            {
                HttpContent content = new ByteArrayContent(File.ReadAllBytes("mydocument.pdf"));
                content.Headers.ContentType = new MediaTypeHeaderValue("application/pdf");
                HttpResponseMessage response =
                    await client.PostAsync("https://test.signicat.com/doc/shared/sds", content);

                string documentId = await response.Content.ReadAsStringAsync();

                Assert.AreEqual(HttpStatusCode.Created, response.StatusCode);
                Assert.IsTrue(documentId.Length > 0);
            }
        }

        [TestMethod]
        public async Task Downloading_a_PDF_document_from_SDS()
        {
            var httpClientHandler = new HttpClientHandler { Credentials = new NetworkCredential("shared", "Bond007") };
            using (var client = new HttpClient(httpClientHandler))
            {
                HttpResponseMessage response =
                    await client.GetAsync("https://test.signicat.com/doc/shared/sds/26082013589fl9ppby4c7ltrgyp11kx41bc9mikazp2yhcsk11fgxfgxtd");

                byte[] pdf = await response.Content.ReadAsByteArrayAsync();
                File.WriteAllBytes("mydownloadedfile.pdf", pdf);

                Assert.AreEqual(HttpStatusCode.OK, response.StatusCode);
                Assert.AreEqual("application/pdf", response.Content.Headers.ContentType.ToString());
            }
        }
    }
}
