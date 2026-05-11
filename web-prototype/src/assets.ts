// Asset loader for Bobby Carrot game
export interface SpriteSheet {
  image: HTMLImageElement;
  frameWidth: number;
  frameHeight: number;
  frames: number;
}

export interface GameAssets {
  tiles: HTMLImageElement;
  tileObjects: HTMLImageElement;
  bobby: HTMLImageElement[];
  bobbyLeft: HTMLImageElement[];
  bobbyRight: HTMLImageElement[];
  bobbyUp: HTMLImageElement[];
  bobbyDown: HTMLImageElement[];
  title: HTMLImageElement;
  logo: HTMLImageElement;
  hud: HTMLImageElement;
  numbers: HTMLImageElement;
  font: HTMLImageElement;
  misc: HTMLImageElement;
  arrows: HTMLImageElement;
  sleep: HTMLImageElement;
  mow: HTMLImageElement;
  train: HTMLImageElement;
  alarm: HTMLImageElement;
}

const ASSET_BASE = '/assets/sprites';

function loadImage(src: string): Promise<HTMLImageElement> {
  return new Promise((resolve, reject) => {
    const img = new Image();
    img.onload = () => resolve(img);
    img.onerror = reject;
    img.src = src;
  });
}

export async function loadGameAssets(): Promise<GameAssets> {
  console.log('Loading game assets...');

  const [
    tiles, tileObjects, title, logo, hud, numbers, font, misc, arrows, sleep, mow, train, alarm,
    b0, b1, b2, b3, b4, b5, b6, b7, b8, b9, bf
  ] = await Promise.all([
    loadImage(`${ASSET_BASE}/ts.png`),
    loadImage(`${ASSET_BASE}/ta.png`),
    loadImage(`${ASSET_BASE}/title.png`),
    loadImage(`${ASSET_BASE}/logo.png`),
    loadImage(`${ASSET_BASE}/hud.png`),
    loadImage(`${ASSET_BASE}/numbers.png`),
    loadImage(`${ASSET_BASE}/font.png`),
    loadImage(`${ASSET_BASE}/misc.png`),
    loadImage(`${ASSET_BASE}/arrows.png`),
    loadImage(`${ASSET_BASE}/sleep.png`),
    loadImage(`${ASSET_BASE}/mow.png`),
    loadImage(`${ASSET_BASE}/train.png`),
    loadImage(`${ASSET_BASE}/alarm.png`),
    loadImage(`${ASSET_BASE}/b0.png`),
    loadImage(`${ASSET_BASE}/b1.png`),
    loadImage(`${ASSET_BASE}/b2.png`),
    loadImage(`${ASSET_BASE}/b3.png`),
    loadImage(`${ASSET_BASE}/b4.png`),
    loadImage(`${ASSET_BASE}/b5.png`),
    loadImage(`${ASSET_BASE}/b6.png`),
    loadImage(`${ASSET_BASE}/b7.png`),
    loadImage(`${ASSET_BASE}/b8.png`),
    loadImage(`${ASSET_BASE}/b9.png`),
    loadImage(`${ASSET_BASE}/bf.png`),
  ]);

  // b0-b3: down animation (4 frames per sprite sheet, 4 sheets = 16 frames)
  // b4: left animation (16 frames)
  // b5: right animation (16 frames)
  // b6: up animation (12 frames)
  // b7: up animation continued (12 frames)
  // b8: death animation (12 frames)
  // b9: various animations (20 frames)
  // bf: falling animation (3 frames)

  const bobbyDown = [b0, b1, b2, b3];
  const bobbyLeft = [b4];
  const bobbyRight = [b5];
  const bobbyUp = [b6, b7];

  console.log('Assets loaded successfully!');

  return {
    tiles,
    tileObjects,
    bobby: [b0, b1, b2, b3, b4, b5, b6, b7, b8, b9, bf],
    bobbyDown,
    bobbyLeft,
    bobbyRight,
    bobbyUp,
    title,
    logo,
    hud,
    numbers,
    font,
    misc,
    arrows,
    sleep,
    mow,
    train,
    alarm,
  };
}

// Audio manager
export class AudioManager {
  private audioContext: AudioContext | null = null;
  private midiFiles: Map<string, string> = new Map();
  private currentMidi: string | null = null;

  constructor() {
    this.midiFiles.set('title', '/assets/audio/title.mid');
    this.midiFiles.set('ingame0', '/assets/audio/ingame0.mid');
    this.midiFiles.set('ingame1', '/assets/audio/ingame1.mid');
    this.midiFiles.set('ingame2', '/assets/audio/ingame2.mid');
    this.midiFiles.set('cleared', '/assets/audio/cleared.mid');
    this.midiFiles.set('death', '/assets/audio/death.mid');
    this.midiFiles.set('bonus', '/assets/audio/bonus.mid');
  }

  // Note: MIDI playback in browser requires a MIDI synthesizer library
  // For now, we'll use Web Audio API for simple sound effects
  // Real MIDI playback would need a library like @magenta/music or JSMIDI

  playBGM(name: string) {
    console.log(`Playing BGM: ${name}`);
    // MIDI playback would go here
    this.currentMidi = name;
  }

  stopBGM() {
    this.currentMidi = null;
  }

  playSFX(name: string) {
    console.log(`Playing SFX: ${name}`);
    // Sound effects would go here
  }
}

// Tile extractor - extracts individual tiles from the tile sheet
export class TileExtractor {
  private tiles: Map<string, ImageData> = new Map();

  constructor(private tileSheet: HTMLImageElement) {}

  // ts.png is 512x512, tiles appear to be 16x16 or 32x32
  getTile(x: number, y: number, size: number = 32): { sx: number; sy: number; sw: number; sh: number } {
    return {
      sx: x * size,
      sy: y * size,
      sw: size,
      sh: size
    };
  }
}

// Sprite animator
export class SpriteAnimator {
  private frame: number = 0;
  private frameCount: number = 0;
  private frameDelay: number = 5;

  constructor(
    private sprites: HTMLImageElement[],
    private framesPerSprite: number = 4,
    private frameWidth: number = 16,
    private frameHeight: number = 48
  ) {}

  update(): void {
    this.frameCount++;
    if (this.frameCount >= this.frameDelay) {
      this.frameCount = 0;
      this.frame = (this.frame + 1) % (this.sprites.length * this.framesPerSprite);
    }
  }

  getCurrentFrame(): { sprite: HTMLImageElement; frame: number } {
    const spriteIndex = Math.floor(this.frame / this.framesPerSprite);
    const localFrame = this.frame % this.framesPerSprite;
    return {
      sprite: this.sprites[spriteIndex % this.sprites.length],
      frame: localFrame
    };
  }

  reset(): void {
    this.frame = 0;
    this.frameCount = 0;
  }
}