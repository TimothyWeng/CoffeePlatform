from PIL import Image
i=1010
img = Image.open(f"src/image/{i}.jpg")
(w, h) = img.size
print('w=%d, h=%d', w, h)

new_img = img.resize((256, 256))
# new_img.show()
new_img.save(f"src/image/{i}-1.png")