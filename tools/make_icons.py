#!/usr/bin/env python3
"""Generate Noor launcher icons (legacy PNG mipmaps) and a preview image."""
import math
import os
from PIL import Image, ImageDraw

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
RES = os.path.join(ROOT, "noor/src/main/res")
SS = 4  # supersampling factor

EMERALD_TOP = (27, 94, 74)
EMERALD_MID = (10, 50, 38)
EMERALD_BOT = (4, 22, 16)
GOLD_LIGHT = (255, 240, 196)
GOLD_MID = (235, 208, 138)
GOLD_DEEP = (201, 162, 75)


def lerp(a, b, t):
    return tuple(int(round(a[i] + (b[i] - a[i]) * t)) for i in range(3))


def background(size):
    """Radial-ish emerald gradient with a warm glow in the upper right."""
    img = Image.new("RGB", (size, size))
    px = img.load()
    gx, gy = size * 0.74, size * 0.26
    maxd = math.hypot(size, size)
    for y in range(size):
        for x in range(size):
            # base vertical/radial emerald blend
            d = math.hypot(x - size * 0.15, y - size * 0.05) / maxd
            t = min(1.0, d * 1.35)
            base = lerp(EMERALD_TOP, EMERALD_MID, t) if t < 0.65 else lerp(EMERALD_MID, EMERALD_BOT, (t - 0.65) / 0.35)
            # warm glow
            gd = math.hypot(x - gx, y - gy) / (size * 0.62)
            if gd < 1.0:
                glow = (1.0 - gd) ** 2 * 0.30
                base = lerp(base, GOLD_LIGHT, glow)
            px[x, y] = base
    return img


def gold_gradient(size):
    img = Image.new("RGB", (size, size))
    px = img.load()
    for y in range(size):
        for x in range(size):
            t = min(1.0, max(0.0, (x * 0.4 + y * 0.6) / size))
            c = lerp(GOLD_LIGHT, GOLD_DEEP, t) if t < 0.55 else lerp(GOLD_MID, GOLD_DEEP, (t - 0.55) / 0.45)
            px[x, y] = c
    return img


def crescent_mask(size):
    m = Image.new("L", (size, size), 0)
    d = ImageDraw.Draw(m)
    cx, cy, r = size * 0.462, size * 0.518, size * 0.232
    hx, hy, hr = size * 0.592, size * 0.472, size * 0.208
    d.ellipse([cx - r, cy - r, cx + r, cy + r], fill=255)
    d.ellipse([hx - hr, hy - hr, hx + hr, hy + hr], fill=0)
    return m


def star_polygon(cx, cy, r, waist=0.20):
    """A four point sparkle: thin diamond arms with a narrow waist."""
    k = r * waist
    pts = [(cx, cy - r), (cx + k, cy - k), (cx + r, cy), (cx + k, cy + k),
           (cx, cy + r), (cx - k, cy + k), (cx - r, cy), (cx - k, cy - k)]
    return pts


def icon(size, round_icon=False):
    big = size * SS
    img = background(big)

    # gold elements on a transparent layer, then composited over the background
    layer = Image.new("RGBA", (big, big), (0, 0, 0, 0))
    layer.paste(gold_gradient(big), (0, 0), crescent_mask(big))

    d = ImageDraw.Draw(layer)
    d.polygon(star_polygon(big * 0.690, big * 0.630, big * 0.078), fill=GOLD_LIGHT + (255,))
    d.polygon(star_polygon(big * 0.756, big * 0.440, big * 0.052), fill=GOLD_LIGHT + (255,))
    d.ellipse(
        [big * 0.605 - big * 0.020, big * 0.760 - big * 0.020,
         big * 0.605 + big * 0.020, big * 0.760 + big * 0.020],
        fill=GOLD_MID + (225,),
    )

    img = Image.alpha_composite(img.convert("RGBA"), layer)

    # final mask (rounded square or circle)
    shape = Image.new("L", (big, big), 0)
    sd = ImageDraw.Draw(shape)
    if round_icon:
        sd.ellipse([0, 0, big - 1, big - 1], fill=255)
    else:
        sd.rounded_rectangle([0, 0, big - 1, big - 1], radius=big * 0.235, fill=255)

    out = Image.new("RGBA", (big, big), (0, 0, 0, 0))
    out.paste(img, (0, 0), shape)
    return out.resize((size, size), Image.LANCZOS)


DENSITIES = [("mdpi", 48), ("hdpi", 72), ("xhdpi", 96), ("xxhdpi", 144), ("xxxhdpi", 192)]

for name, size in DENSITIES:
    folder = os.path.join(RES, f"mipmap-{name}")
    os.makedirs(folder, exist_ok=True)
    icon(size, False).save(os.path.join(folder, "ic_launcher.png"))
    icon(size, True).save(os.path.join(folder, "ic_launcher_round.png"))
    print("wrote", folder, size)

os.makedirs(os.path.join(ROOT, "docs"), exist_ok=True)
icon(512, False).save(os.path.join(ROOT, "docs/noor-icon.png"))
icon(256, True).save(os.path.join(ROOT, "docs/noor-icon-round.png"))
print("wrote docs/noor-icon.png")
