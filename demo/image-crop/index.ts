import Navigation from 'hybrid-navigation';

import ImageCropDemo from './ImageCropDemo';
import ImageCropPage from './ImageCropPage';
import ImageCropResultPage from './ImageCropResultPage';

export function registerImageCropComponent() {
  Navigation.registerComponent('ImageCropDemo', () => ImageCropDemo);
  Navigation.registerComponent('ImageCropPage', () => ImageCropPage);
  Navigation.registerComponent('ImageCropResultPage', () => ImageCropResultPage);
}
