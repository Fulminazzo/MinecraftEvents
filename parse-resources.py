#!/usr/bin/env python3
"""
	A script to merge together all
	the resources of subprojects to the
	root project.
"""
import os
import shutil
import yaml

ENCODING="utf-8"

def get_folders(path: str) -> list:
	contents = os.listdir(path)
	return [f for f in contents if os.path.isdir(os.path.join(path, f)) and not f.startswith(".")]

def get_project_folders(path: str) -> list:
	contents = get_folders(path)
	for f in ["gradle", "build"]:
		contents.remove(f)
	return contents

def get_yaml_resources(path: str) -> list:
	resources = []
	directory = os.path.join(path, "src/main/resources")
	if not os.path.isdir(directory): return resources
	for f in os.listdir():
		if f.endswith(".yml"): resources.append(f)
	return resources

def parse_yaml(contents1: dict, contents2: dict):
	for (k, v) in contents2.items():
		if not k in contents1:
			contents1[k] = v
		else:
			prev = contents1[k]
			if isinstance(prev, dict):
				parse_yaml(contents1[k], contents2[k])


def parse_resource(resource: str, modules: list):
	main_file = "src/main/resources/" + resource
	existed = True
	if not os.path.isfile(main_file):
		file = open(main_file, "w", encoding=ENCODING)
		file.close()
		existed = False

	with open(main_file, "r", encoding=ENCODING) as file:
		contents = yaml.safe_load(file)
		if contents is None: contents = {}

	for module in modules:
		module_resource = module + "/" + main_file
		if not os.path.isfile(module_resource): continue
		with open(module_resource, "r", encoding=ENCODING) as tmp:
			contents1 = yaml.safe_load(tmp)
		parse_yaml(contents, contents1)

	if existed:
		shutil.copyfile(main_file, main_file.replace("resources/", "resources/original-"))

	with open(main_file, "w", encoding=ENCODING) as file:
		yaml.dump(contents, file, allow_unicode=True, width=float("inf"))


	with open(main_file, "r", encoding=ENCODING) as file:
		lines = file.readlines()
		file.close()

		file = open(main_file, "w", encoding=ENCODING)
		banner = """#####################################################
#  THIS FILE HAS BEEN GENERATED AUTOMATICALLY       #
#                                                   #
#  Please, if you want to edit some values here,    #
#  use the corresponding files in the respective    #
# modules, or edit one of the original-filename.yml #
#   found in this project resources directory.      #
#                                                   #
#####################################################"""
		file.write(banner + "\n")
		for line in lines:
			file.write(line)

def reset():
	resources = "src/main/resources"

	files = os.listdir(resources)
	if len([f for f in files if f.startswith("original-")]) == 0: return
	for file in files:
		if not file.endswith(".yml"): continue
		if file.startswith("original-"): continue
		os.remove(os.path.join(resources, file))

	for file in os.listdir(resources):
		if not file.endswith(".yml"): continue
		name = file.split("-")[1]
		os.rename(os.path.join(resources, file), os.path.join(resources, name))

def main():
	cwd = os.getcwd()
	project = get_project_folders(cwd)
	if len(project) == 0:
		print("Not Java project or could not find folders.")
		print("Are you in the root directory?")
		return
	if len(project) == 1:
		print("Not multi-module project!")
		return
	if not "src" in project:
		print("Could not find src folder.")
		print("Are you in the root directory?")
		return

	reset()
	modules = [f for f in project if not f == "src" and "src" in os.listdir(os.path.join(cwd, f))]

	resources = set()
	for module in ["", *modules]:
		for r in get_yaml_resources(module):
			resources.add(r)

	for resource in resources:
		parse_resource(resource, modules)

if __name__ == '__main__':
	main()