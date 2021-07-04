# pip3 install dnspython3

import dnspython as dns
import dns.resolver

import asyncio


async def getMXRecord(domain: str):
	results = []
	result = await dns.resolver.query(domain, 'MX')
	for mx in result:
		results.append(mx.to_text())
	return results

async def main():
	await asyncio.wait([
		getMXRecord("youtube.com"),
		getMXRecord("medium.com"),
		getMXRecord("reddit.com"),
	])

loop = asyncio.get_event_loop()
loop.run_until_complete(main())
loop.close()
