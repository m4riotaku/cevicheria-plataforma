
<?php
declare(strict_types=1);

$repo = '/home/cevicherias/repositories/cevicheria-plataforma';
$private = '/home/cevicherias/private';
$secretFile = $private . '/webhook.secret';

header('Content-Type: text/plain; charset=UTF-8');

if (($_SERVER['REQUEST_METHOD'] ?? '') !== 'POST') {
    http_response_code(405);
    exit('Method not allowed');
}

$secret = @file_get_contents($secretFile);

if ($secret === false || trim($secret) === '') {
    http_response_code(500);
    exit('Webhook secret not configured');
}

$payload = file_get_contents('php://input');

if ($payload === false) {
    http_response_code(400);
    exit('Invalid payload');
}

$signature = $_SERVER['HTTP_X_HUB_SIGNATURE_256'] ?? '';

$expected = 'sha256=' . hash_hmac(
    'sha256',
    $payload,
    trim($secret)
);

if (!hash_equals($expected, $signature)) {
    http_response_code(403);
    exit('Invalid signature');
}

$event = $_SERVER['HTTP_X_GITHUB_EVENT'] ?? '';

if ($event === 'ping') {
    http_response_code(200);
    exit('OK');
}

$data = json_decode($payload, true);

if (
    !is_array($data) ||
    $event !== 'push' ||
    ($data['ref'] ?? '') !== 'refs/heads/main' ||
    ($data['repository']['full_name'] ?? '') !== 'm4riotaku/cevicheria-plataforma' ||
    ($data['deleted'] ?? false)
) {
    http_response_code(202);
    exit('Ignored');
}

if (!function_exists('exec')) {
    http_response_code(500);
    exit('Command execution disabled');
}

$script = 'set -e; ' .
    '/usr/bin/git -C ' . escapeshellarg($repo) .
    ' pull --ff-only origin main; ' .
    '/usr/local/cpanel/bin/uapi --output=json ' .
    'VersionControlDeployment create repository_root=' .
    escapeshellarg($repo);

$command = '/usr/bin/nohup /usr/bin/flock -w 300 ' .
    escapeshellarg($private . '/deploy.lock') .
    ' /bin/bash -lc ' . escapeshellarg($script) .
    ' >> ' . escapeshellarg($private . '/deploy.log') .
    ' 2>&1 < /dev/null &';

exec($command);

http_response_code(202);
echo 'Deployment requested';
