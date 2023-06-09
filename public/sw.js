self.addEventListener('install', e => {
    // init
});

self.addEventListener('activate', event => {
    event.waitUntil(self.clients.claim());
});

const CACHE = "v1";

self.addEventListener('fetch', function (evt) {
    // console.log(evt.request.url, evt.request);
    if (
        evt.request.url.indexOf('data:') !== 0
        && evt.request.url.indexOf('chrome-extension://') !== 0
        && evt.request.url.indexOf('https://www.googleapis.com') !== 0
        && evt.request.url.indexOf('/service/') !== 0
    )
    evt.respondWith(fromNetwork(evt.request, 400).catch(function () {
        return fromCache(evt.request);
    }));
});

function fromNetwork(request, timeout) {
    return new Promise(function (fulfill, reject) {

        var timeoutId = setTimeout(reject, timeout);
        var fetchRequest = request.clone();

        fetch(fetchRequest).then(function (response) {
            clearTimeout(timeoutId);
            update(request)
            fulfill(response);

        }, reject);
    });
}

function fromCache(request) {
    return caches.open(CACHE).then(function (cache) {
        return cache.match(request).then(function (matching) {
            return matching || Promise.reject('no-match');
        });
    });
}

function update(request) {
  return caches.open(CACHE).then(function (cache) {
    return fetch(request).then(function (response) {
      return cache.put(request, response.clone()).then(function () {
        return response;
      });
    });
  });
}